package com.highershine.portal.common.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.constants.RoleConstant;
import com.highershine.portal.common.entity.bo.ClientRoleBo;
import com.highershine.portal.common.entity.dto.SysUserDTO;
import com.highershine.portal.common.entity.dto.SysUserListDTO;
import com.highershine.portal.common.entity.dto.UpdatePasswordDTO;
import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.po.SysUserRole;
import com.highershine.portal.common.entity.vo.FindSysUserVo;
import com.highershine.portal.common.entity.vo.SysUserListVo;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.mapper.SysUserMapper;
import com.highershine.portal.common.mapper.SysUserRoleMapper;
import com.highershine.portal.common.utils.DateTools;
import com.highershine.portal.common.utils.JSONUtil;
import com.highershine.portal.common.utils.URLConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Value("${oauth2.server.clientId:website}")
    private String clientId;
    @Value("${interface.validPersonInLab.addr}")
    private String validPersonInLabAddr;
    @Value("${interface.userSyncStatistics.addr}")
    private String userSyncStatisticsAddr;


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
        sysUserMapper.updatePassword(dto.getId(), dto.getPassword());
    }

    /**
     * 用户注册
     * @param dto
     * @return
     */
    @Override
    public SysUser register(SysUserDTO dto) throws Exception {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(dto, sysUser);
        //管辖地区
        String serverNos = sysUser.getProvince();
        String serverNo = sysUser.getProvince();
        if (StringUtils.isNotBlank(sysUser.getArea())) {
            serverNos += "," + sysUser.getCity() + "," + sysUser.getArea();
            serverNo = sysUser.getArea();
        } else if (StringUtils.isNotBlank(sysUser.getCity())) {
            serverNos += "," + sysUser.getCity();
            serverNo = sysUser.getCity();
        }
        sysUser.setServerNos(serverNos);
        //是否新增实验室
        if (sysUser.getIsAddOrg() == null) {
            sysUser.setIsAddOrg(false);
        }
        //是否启用
        if (sysUser.getStatus() == null) {
            sysUser.setStatus(true);
        }
        sysUser.setDeleteFlag(false).setCreateDatetime(new Date()).setUpdateDatetime(new Date());
        sysUserMapper.insert(sysUser);
        //修改ext_id(登录子系统使用)
        sysUserMapper.updateExtId(sysUser.getId());
        //门户系统用户角色初始化
        if (CollectionUtils.isEmpty(dto.getClientRoles())) {
            ArrayList<ClientRoleBo> clientRoles = new ArrayList<>();
            ClientRoleBo clientRoleBo = new ClientRoleBo();
            //门户系统 普通用户
            clientRoleBo.setClientId(clientId);
            List<String> roleList = new ArrayList<>(1);
            roleList.add(RoleConstant.ROLE_AVERAGE);
            clientRoleBo.setRoles(roleList);
            clientRoles.add(clientRoleBo);
            dto.setClientRoles(clientRoles);
        }
        // 插入角色信息
        String roles = "";
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (ClientRoleBo clientRole : dto.getClientRoles()) {
            String client = clientRole.getClientId();
            for (String role : clientRole.getRoles()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setClientId(client).setRoleId(role).setUserId(sysUser.getId());
                if (client.equals(clientId)) {
                    roles += RoleConstant.getRoleExtId(role) + ",";
                }
                sysUserRoleList.add(sysUserRole);
            }
        }
        sysUserRoleMapper.batchInsert(sysUserRoleList);
        //用户同步到填报系统
        JSONObject json = getUserSyncParam(sysUser);
        json.put("roles", roles);
        json.put("serverNo", serverNo);
        String result = URLConnectionUtil.post(userSyncStatisticsAddr, json.toString());
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("the url return is blank:" + userSyncStatisticsAddr);
        }
        Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
        Integer code = (Integer) resultMap.get("code");
        if (!ResultEnum.SUCCESS_STATUS.getCode().equals(code)) {
            throw new RuntimeException("the url return code is not success:" + userSyncStatisticsAddr);
        }
        return sysUser;
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
        paramMap.put("createdBy", sysUser.getCreateDatetime());
        paramMap.put("updatedBy", sysUser.getUpdateDatetime());
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
    public String registerAndValid(SysUserDTO dto) throws Exception {
        SysUser sysUser = sysUserMapper.selectByUsername(dto.getUsername());
        if (sysUser != null) {
            return "该用户名已被注册";
        }
        sysUser = sysUserMapper.selectByIdCardNo(dto.getIdCardNo());
        if (sysUser != null) {
            return "该身份证号已被注册";
        }
        //判断工作岗位，每个实验室【负责人、数据库管理员】只能注册一个人
        if (!dto.getJob().equals("3")) {
            int count = sysUserMapper.selectByOrgCodeJob(dto.getOrgCode(), dto.getJob());
            if (count != 0) {
                return "该实验室此工作岗位已被注册";
            }
        }
        //校验用户是否是选择实验室的人员
        JSONObject json = new JSONObject();
        json.put("orgCode", dto.getOrgCode());
        json.put("fullname", dto.getNickname());
        json.put("idCardNo", dto.getIdCardNo());
        String result = URLConnectionUtil.post(validPersonInLabAddr, json.toString());
        if (StringUtils.isBlank(result)) {
            throw new RuntimeException("the url return is blank:" + validPersonInLabAddr);
        }
        Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
        Integer code = (Integer) resultMap.get("code");
        Map<String, Object> data = (Map) resultMap.get("data");
        if (!ResultEnum.SUCCESS_STATUS.getCode().equals(code)) {
            throw new RuntimeException("the url return code is not success:" + validPersonInLabAddr);
        } else if ((boolean) data.get("existsFlag") == false){
            return "您不属于该实验室，请联系管理员";
        }
        // 判断手动输入所在单位编号是否重复
        if (sysUser.getIsAddOrg() != null && sysUser.getIsAddOrg()) {
            String provinceSubCode = sysUser.getProvince().substring(0, 4);
            String orgSubCode = sysUser.getOrgCode().substring(0, 4);
            if (!orgSubCode.equals(provinceSubCode)) {
                return "【公安机构代码】与【所属行政地区/单位】不匹配，请检查";
            }
        }
        //注册
        register(dto);
        return null;
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
    public void deleteUserById(Long id) {
        sysUserMapper.deleteUserById(id);
    }
}
