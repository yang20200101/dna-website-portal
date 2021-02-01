package com.highershine.portal.common.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.constants.CommonConstant;
import com.highershine.portal.common.constants.RedisConstant;
import com.highershine.portal.common.converter.InterfacePropertyConverter;
import com.highershine.portal.common.converter.SysUserConverter;
import com.highershine.portal.common.entity.bo.ClientRoleBo;
import com.highershine.portal.common.entity.dto.SysUserDTO;
import com.highershine.portal.common.entity.dto.SysUserListDTO;
import com.highershine.portal.common.entity.dto.UpdatePasswordDTO;
import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.po.SysUserRole;
import com.highershine.portal.common.entity.vo.FindSysUserVo;
import com.highershine.portal.common.entity.vo.SysUserListVo;
import com.highershine.portal.common.enums.HttpStatusEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.exception.RegisterException;
import com.highershine.portal.common.mapper.SysUserMapper;
import com.highershine.portal.common.mapper.SysUserRoleMapper;
import com.highershine.portal.common.utils.DateTools;
import com.highershine.portal.common.utils.HttpUtils;
import com.highershine.portal.common.utils.JSONUtil;
import com.highershine.portal.common.utils.URLConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 用户服务层
 * @Author: mizhanlei
 * @Date: 2019/11/26 17:57
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysRegionalismService regionalismService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ValueOperations valueOperations;
    @Value("${oauth2.server.clientId:website}")
    private String clientId;
    @Autowired
    private InterfacePropertyConverter urls;
    @Value("${person.query.addr}")
    private String personQueryAddr;
    @Value("${person.query.salt}")
    private String personQuerySalt;
    @Value("${person.query.systemkey}")
    private String personQuerySystemkey;


    /**
     * 查询oauth回调地址
     * @return
     */
    @Override
    public String selectOauthRedirectUri(String clientId) {
        return this.sysUserMapper.selectOauthRedirectUri(clientId);
    }

    /**
     * 重置密码
     * @param id
     */
    @Override
    public void resetPassword(Long id) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //encodeResult 为需要存入数据中加盐加密后的密码
        String encodeResult = bCryptPasswordEncoder.encode("111111");
        this.sysUserMapper.updatePassword(id, encodeResult);
        //清楚redis中token
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        if (sysUser != null) {
            cleanJwtRedis(sysUser.getUsername());
        }
    }

    /**
     * 校验原始密码
     * @param dto
     * @return
     */
    @Override
    public boolean validSrcPassword(UpdatePasswordDTO dto) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(dto.getId());
        if (BCrypt.checkpw(dto.getSrcPassword(), sysUser.getPassword())) {
            //原密码正确
            return true;
        }
        return false;
    }

    /**
     * 修改密码
     * @param dto
     */
    @Override
    public void updatePassword(UpdatePasswordDTO dto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //encodeResult 为需要存入数据中加盐加密后的密码
        String encodeResult = bCryptPasswordEncoder.encode(dto.getPassword());
        sysUserMapper.updatePassword(dto.getId(), encodeResult);
        //清除redis中token
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(dto.getId());
        if (sysUser != null) {
            cleanJwtRedis(sysUser.getUsername());
        }
    }

    /**
     * 用户注册
     * @param dto
     * @return
     */
    @Override
    public SysUser register(SysUserDTO dto) throws Exception {
        SysUser sysUser = SysUserConverter.transferSysUserDto2Po(dto);
        sysUserMapper.insert(sysUser);
        //修改ext_id(登录子系统使用)
        sysUserMapper.updateExtId(sysUser.getId());
        // 插入角色信息
        dto.setId(sysUser.getId());
        List<SysUserRole> sysUserRoleList = SysUserConverter.transferSysUserDto2UserRole(dto, CommonConstant.CLIENT_WEBSITE);
        sysUserRoleMapper.batchInsert(sysUserRoleList);
        //用户同步到填报系统
        JSONObject json = getUserSyncParam(sysUser);
        json.put("roles", dto.getRoles());
        json.put("serverNo", dto.getServerNo());
        String result = URLConnectionUtil.post(urls.getUserSyncStatisticsUrl(), json.toString());
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("the url return is blank:" + urls.getUserSyncStatisticsUrl());
        }
        Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
        Integer code = ((Long) resultMap.get("code")).intValue();
        if (!ResultEnum.SUCCESS_STATUS.getCode().equals(code)) {
            throw new RuntimeException("the url return code is not success:" + urls.getUserSyncStatisticsUrl());
        }
        return sysUser;
    }


    /**
     * 修改用户
     * @param dto
     * @return
     */
    @Override
    public void updateUser(SysUserDTO dto) {
        SysUser sysUser = SysUserConverter.transferSysUserDto2Po(dto);
        sysUserMapper.updateByPrimaryKey(sysUser);
        // 删除角色信息
        sysUserRoleMapper.deleteByUserId(dto.getId());
        // 插入角色信息
        List<SysUserRole> sysUserRoleList = SysUserConverter.transferSysUserDto2UserRole(dto, CommonConstant.CLIENT_WEBSITE);
        sysUserRoleMapper.batchInsert(sysUserRoleList);
    }

    /**
     * 用户同步参数
     * @param sysUser
     * @return
     */
    private JSONObject getUserSyncParam(SysUser sysUser) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("id", sysUser.getId());
        paramMap.put("subSystem", "1");
        paramMap.put("username", sysUser.getUsername());
        paramMap.put("nickname", sysUser.getNickname());
        paramMap.put("province", sysUser.getProvince());
        paramMap.put("orgCode", sysUser.getOrgCode());
        paramMap.put("status", true);
        paramMap.put("deleted", false);
        paramMap.put("password", "");
        paramMap.put("createdBy", DateTools.dateToString(sysUser.getCreateDatetime(), DateTools.DF_TIME));
        paramMap.put("updatedBy", DateTools.dateToString(sysUser.getUpdateDatetime(), DateTools.DF_TIME));
        paramMap.put("birthDate", sysUser.getBirthDate());
        paramMap.put("gender", sysUser.getGender());
        paramMap.put("idCardNo", sysUser.getIdCardNo());
        paramMap.put("phone", sysUser.getPhone());
        paramMap.put("labJobKey", sysUser.getJob());
        return paramMap;
    }

    /**
     * 用户校验和注册
     * @param dto
     * @return
     */
    @Override
    public SysUser registerAndValid(SysUserDTO dto) throws Exception {
        //注册校验
        registerValid(dto);
        //注册
        return register(dto);
    }

    /**
     * 修改用户信息和校验
     * @param dto
     * @throws Exception
     */
    @Override
    public void updateUserAndValid(SysUserDTO dto) throws Exception {
        //用户信息校验
        registerValid(dto);
        //修改用户信息
        updateUser(dto);
        //清除redis中token
        cleanJwtRedis(dto.getUsername());
    }

    /**
     * 注册校验
     * @param dto
     * @return
     * @throws Exception
     */
    public void registerValid(SysUserDTO dto) throws Exception {
        if (dto.getId() == null && StringUtils.isBlank(dto.getPassword())) {
            throw new RegisterException("密码为空");
        }
        SysUser sysUser = sysUserMapper.selectByUsername(dto);
        if (sysUser != null) {
            throw new RegisterException("该用户名已被注册");
        }
        sysUser = sysUserMapper.selectByIdCardNo(dto);
        if (sysUser != null) {
            throw new RegisterException("该身份证号已被注册");
        }
        //判断工作岗位，每个实验室【负责人、数据库管理员】只能注册一个人
        if (!dto.getJob().equals("3")) {
            int count = sysUserMapper.selectByOrgCodeJob(dto);
            if (count != 0) {
                throw new RegisterException("该实验室此工作岗位已被注册");
            }
        }
        //校验用户是否是选择实验室的人员
        JSONObject json = new JSONObject();
        json.put("orgCode", dto.getOrgCode());
        json.put("fullname", dto.getNickname());
        json.put("idCardNo", dto.getIdCardNo());
        json.put("job", dto.getJob());
        String result = URLConnectionUtil.post(urls.getValidPersonInLabUrl(), json.toString());
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("the url return is blank:" + urls.getValidPersonInLabUrl());
        }
        Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
        Integer code = ((Long) resultMap.get("code")).intValue();
        Map<String, Object> data = (Map) resultMap.get("data");
        if (!ResultEnum.SUCCESS_STATUS.getCode().equals(code)) {
            throw new RuntimeException("the url return code is not success:" + urls.getValidPersonInLabUrl());
        } else if ((boolean) data.get("existsFlag") == false){
            throw new RegisterException("您不属于该实验室，请联系管理员");
        }
        // 判断手动输入所在单位编号是否重复
        if (dto.getIsAddOrg() != null && dto.getIsAddOrg()) {
            String serverNo = dto.getProvince();
            if (StringUtils.isNotBlank(dto.getArea())) {
                serverNo = dto.getArea();
            } else if (StringUtils.isNotBlank(dto.getCity())) {
                serverNo = dto.getCity();
            }
            String serverCode = serverNo.substring(0, 4);
            String orgSubCode = dto.getOrgCode().substring(0, 4);
            if (!orgSubCode.equals(serverCode)) {
                throw new RegisterException("【公安机构代码】与【所属行政地区/单位】不匹配，请检查");
            }
        }
        //查询户籍系统 判断身份证号姓名是否匹配
        Map paramMap = new HashMap<String, Object>();
        paramMap.put("cardNo", dto.getIdCardNo());
        String signCode = DigestUtils.md5DigestAsHex((dto.getIdCardNo() + "&&" + personQuerySystemkey
                + "&&" + personQuerySalt).getBytes());
        paramMap.put("signCode", signCode);
        paramMap.put("systemKey", personQuerySystemkey);
        result = HttpUtils.doPost(personQueryAddr, paramMap);
        if (StringUtils.isNotBlank(result)) {
            resultMap = JSONUtil.parseJsonToMap(result);
            code = ((Long) resultMap.get("code")).intValue();
            if (ResultEnum.SUCCESS_STATUS.getCode().equals(code)) {
                List<HashMap<String, String>> resultData = (List<HashMap<String, String>>) resultMap.get("data");
                if (resultData != null && resultData.size() > 0) {
                    HashMap<String, String> stringStringHashMap = resultData.get(0);
                    if (!dto.getNickname().equals(stringStringHashMap.get("xm"))) {
                        throw new RegisterException("姓名与身份证号不匹配，请根据身份证号进行提取'");
                    }
                } else {
                    throw new RegisterException("该身份证号在户籍系统中不存在，请重新填写后进行提取");
                }
            }
        }
        //生成实验室编号
        if (dto.getIsAddOrg() != null && dto.getIsAddOrg()) {
            result = URLConnectionUtil.get(urls.getSaveLabUrl() + "?labCode="
                    + dto.getOrgCode() + "&labName=" + URLEncoder.encode(dto.getLabName(),"UTF-8"), null);
            if (StringUtils.isBlank(result)) {
                throw new RuntimeException("the url return is blank:" + urls.getSaveLabUrl());
            }
            resultMap = JSONUtil.parseJsonToMap(result);
            code = ((Long) resultMap.get("code")).intValue();
            if (!HttpStatusEnum.OK.getCode().equals(code)) {
                if (HttpStatusEnum.PRECONDITION_FAILED.getCode().equals(code)) {
                    throw new RegisterException((String) resultMap.get("msg"));
                }
                throw new RuntimeException("the url return code is not success:" + urls.getSaveLabUrl());
            }
        }

    }

    @Override
    public FindSysUserVo findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        FindSysUserVo vo = new FindSysUserVo();
        BeanUtils.copyProperties(sysUser, vo);
        // 管辖地区
        String[] arrs = sysUser.getServerNos().split(",");
        vo.setServerNoList(Arrays.asList(arrs));
        // 客户端角色信息
        List<ClientRoleBo> clientRoles = sysUserRoleMapper.selectClientRoleByUserId(id);
        vo.setClientRoles(clientRoles);
        return vo;
    }

    /**
     * 查询用户列表
     * @param dto
     * @return
     */
    @Override
    public PageInfo<SysUserListVo> getUserList(SysUserListDTO dto) {
        PageHelper.startPage(dto.getCurrent(), dto.getPageSize());
        List<SysUserListVo> list = sysUserMapper.selectListByDto(dto);
        //所在地区编码转换  更新时间格式转换
        list.stream().forEach(vo ->  vo.setAddress(regionalismService.getNameByCode(vo.getProvince())
                + regionalismService.getNameByCode(vo.getCity()) + regionalismService.getNameByCode(vo.getArea()))
                .setUpdateDateStr(DateTools.dateToString(vo.getUpdateDatetime(), DateTools.DF_MINUTE)));
        PageInfo<SysUserListVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 删除用户
     * @param id
     */
    @Override
    public void deleteUserById(Long id) throws Exception {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        sysUserMapper.deleteUserById(id);
        // 删除子系统用户
        String result = URLConnectionUtil.get(urls.getUserDelStatisticsUrl() + sysUser.getUsername(), null);
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("the url return is blank:" + urls.getUserDelStatisticsUrl());
        }
        Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
        Integer code = ((Long) resultMap.get("code")).intValue();
        if (!ResultEnum.SUCCESS_STATUS.equals(code)) {
            throw new RuntimeException("the url return code is not success:" + urls.getUserDelStatisticsUrl());
        }
    }

    private void cleanJwtRedis (String username) {
        //清除redis中的token
        valueOperations.set(RedisConstant.REDIS_LOGIN + username, "", 1, TimeUnit.MICROSECONDS);
    }
}
