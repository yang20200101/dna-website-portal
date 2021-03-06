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
import com.highershine.portal.common.entity.vo.ProvinceSysUserVo;
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
 * @Description: ???????????????
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
    private SysRoleService sysRoleService;
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
     * ??????oauth????????????
     * @return
     */
    @Override
    public String selectOauthRedirectUri(String clientId) {
        return this.sysUserMapper.selectOauthRedirectUri(clientId);
    }

    /**
     * ????????????
     * @param id
     */
    @Override
    public void resetPassword(Long id) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //encodeResult ????????????????????????????????????????????????
        String encodeResult = bCryptPasswordEncoder.encode("111111");
        this.sysUserMapper.updatePassword(id, encodeResult);
        //??????redis???token
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        if (sysUser != null) {
            cleanJwtRedis(sysUser.getUsername());
        }
    }

    /**
     * ??????????????????
     * @param dto
     * @return
     */
    @Override
    public boolean validSrcPassword(UpdatePasswordDTO dto) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(dto.getId());
        if (BCrypt.checkpw(dto.getSrcPassword(), sysUser.getPassword())) {
            //???????????????
            return true;
        }
        return false;
    }

    /**
     * ????????????
     * @param dto
     */
    @Override
    public void updatePassword(UpdatePasswordDTO dto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //encodeResult ????????????????????????????????????????????????
        String encodeResult = bCryptPasswordEncoder.encode(dto.getPassword());
        sysUserMapper.updatePassword(dto.getId(), encodeResult);
        //??????redis???token
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(dto.getId());
        if (sysUser != null) {
            cleanJwtRedis(sysUser.getUsername());
        }
    }

    /**
     * ????????????
     * @param dto
     * @return
     */
    @Override
    public SysUser register(SysUserDTO dto) throws Exception {
        SysUser sysUser = SysUserConverter.transferSysUserDto2Po(dto);
        sysUserMapper.insert(sysUser);
        //??????ext_id(?????????????????????)
        sysUserMapper.updateExtId(sysUser.getId());
        // ??????????????????
        dto.setId(sysUser.getId());
        List<SysUserRole> sysUserRoleList = SysUserConverter.transferSysUserDto2UserRole(dto, sysRoleService.getRoleList());
        sysUserRoleMapper.batchInsert(sysUserRoleList);
        //???????????????????????????
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
     * ????????????
     * @param dto
     * @return
     */
    @Override
    public void updateUser(SysUserDTO dto, boolean perfectFlag) {
        SysUser sysUser = SysUserConverter.transferSysUserDto2Po(dto);
        sysUserMapper.updateByPrimaryKey(sysUser);
        // ?????????????????????????????????????????????
        if (!perfectFlag) {
            // ??????????????????
            sysUserRoleMapper.deleteByUserId(dto.getId());
            // ??????????????????
            List<SysUserRole> sysUserRoleList = SysUserConverter.transferSysUserDto2UserRole(dto, CommonConstant.CLIENT_WEBSITE);
            sysUserRoleMapper.batchInsert(sysUserRoleList);
        }
    }

    /**
     * ??????????????????
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
     * ?????????????????????
     * @param dto
     * @return
     */
    @Override
    public SysUser registerAndValid(SysUserDTO dto) throws Exception {
        //????????????
        registerValid(dto);
        //??????
        return register(dto);
    }

    /**
     * ???????????????????????????
     * @param dto
     * @throws Exception
     */
    @Override
    public void updateUserAndValid(SysUserDTO dto, boolean perfectFlag) throws Exception {
        //??????????????????
        registerValid(dto);
        //??????????????????
        updateUser(dto, perfectFlag);
        //??????redis???token
        cleanJwtRedis(dto.getUsername());
    }

    /**
     * ????????????
     * @param dto
     * @throws Exception
     */
    public void registerValid(SysUserDTO dto) throws Exception {
        if (dto.getId() == null && StringUtils.isBlank(dto.getPassword())) {
            throw new RegisterException("????????????");
        }
        SysUser sysUser = sysUserMapper.selectByUsername(dto);
        if (sysUser != null) {
            throw new RegisterException("????????????????????????");
        }
        sysUser = sysUserMapper.selectByIdCardNo(dto);
        if (sysUser != null) {
            throw new RegisterException("???????????????????????????");
        }
        //?????????????????????????????????????????????????????????????????????????????????????????????
        if (!dto.getJob().equals("3")) {
            int count = sysUserMapper.selectByOrgCodeJob(dto);
            if (count != 0) {
                throw new RegisterException("???????????????????????????????????????");
            }
        }
        String result = "";
        Integer code = null;
        Map<String, Object> resultMap = null;
        Map<String, Object> data = null;
        SysUser updateUser = null;
        if (dto.getId() != null) {
            updateUser = sysUserMapper.selectByPrimaryKey(dto.getId());
        }
        //?????? ??????  ????????????????????? ??????????????????
        if (dto.getId() == null
                || (updateUser != null && updateUser.getOrgCode().equals(dto.getOrgCode()))) {
            //?????????????????????????????????????????????
            JSONObject json = new JSONObject();
            json.put("orgCode", dto.getOrgCode());
            json.put("fullname", dto.getNickname());
            json.put("idCardNo", dto.getIdCardNo());
            json.put("job", dto.getJob());
            result = URLConnectionUtil.post(urls.getValidPersonInLabUrl(), json.toString());
            if (StringUtils.isBlank(result)) {
                throw new RuntimeException("the url return is blank:" + urls.getValidPersonInLabUrl());
            }
            resultMap = JSONUtil.parseJsonToMap(result);
            code = ((Long) resultMap.get("code")).intValue();
            data = (Map) resultMap.get("data");
            if (!ResultEnum.SUCCESS_STATUS.getCode().equals(code)) {
                throw new RuntimeException("the url return code is not success:" + urls.getValidPersonInLabUrl());
            } else if ((boolean) data.get("existsFlag") == false) {
                throw new RegisterException("?????????????????????????????????????????????");
            }
        }
//        // ????????????????????????????????????????????????
//        if (dto.getIsAddOrg() != null && dto.getIsAddOrg()) {
//            String serverNo = dto.getProvince();
//            if (StringUtils.isNotBlank(dto.getArea())) {
//                serverNo = dto.getArea();
//            } else if (StringUtils.isNotBlank(dto.getCity())) {
//                serverNo = dto.getCity();
//            }
//            String serverCode = serverNo.substring(0, 4);
//            String orgSubCode = dto.getOrgCode().substring(0, 4);
//            if (!orgSubCode.equals(serverCode)) {
//                throw new RegisterException("????????????????????????????????????????????????/??????????????????????????????");
//            }
//        }
        //?????????????????? ????????????????????????????????????
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
                        throw new RegisterException("??????????????????????????????????????????????????????????????????'");
                    }
                } else {
                    throw new RegisterException("???????????????????????????????????????????????????????????????????????????");
                }
            }
        }
        //???????????????
//        if (dto.getIsAddOrg() != null && dto.getIsAddOrg()) {
//            result = URLConnectionUtil.get(urls.getSaveLabUrl() + "?labCode="
//                    + dto.getOrgCode() + "&labName=" + URLEncoder.encode(dto.getLabName(),"UTF-8"), null);
//            if (StringUtils.isBlank(result)) {
//                throw new RuntimeException("the url return is blank:" + urls.getSaveLabUrl());
//            }
//            resultMap = JSONUtil.parseJsonToMap(result);
//            code = ((Long) resultMap.get("code")).intValue();
//            if (!HttpStatusEnum.OK.getCode().equals(code)) {
//                if (HttpStatusEnum.PRECONDITION_FAILED.getCode().equals(code)) {
//                    throw new RegisterException((String) resultMap.get("msg"));
//                }
//                throw new RuntimeException("the url return code is not success:" + urls.getSaveLabUrl());
//            }
//        }
        //???????????????????????????
        if (dto.getIsAddOrg() != null && dto.getIsAddOrg()) {
            String serverNo = dto.getProvince();
            if (StringUtils.isNotBlank(dto.getArea())) {
                serverNo = dto.getArea();
            } else if (StringUtils.isNotBlank(dto.getCity())) {
                serverNo = dto.getCity();
            }
            result = URLConnectionUtil.get(urls.getSaveLabUrl() + "?regionalismCode="
                    + serverNo + "&labName=" + URLEncoder.encode(dto.getLabName(),"UTF-8"), null);
            if (StringUtils.isBlank(result)) {
                throw new RuntimeException("the url return is blank:" + urls.getSaveLabUrl());
            }
            resultMap = JSONUtil.parseJsonToMap(result);
            code = ((Long) resultMap.get("code")).intValue();
            if (!HttpStatusEnum.OK.getCode().equals(code)) {
                if (HttpStatusEnum.NOT_ACCEPTABLE.getCode().equals(code)) {
                    throw new RegisterException((String) resultMap.get("msg"));
                }
                throw new RuntimeException("the url return code is not success:" + urls.getSaveLabUrl());
            } else {
                data = (Map) resultMap.get("data");
                if (StringUtils.isBlank((String) data.get("labCode"))) {
                    throw new RegisterException("?????????????????????");
                }
            }
            dto.setOrgCode((String) data.get("labCode"));
        }
    }

    @Override
    public FindSysUserVo findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        FindSysUserVo vo = new FindSysUserVo();
        BeanUtils.copyProperties(sysUser, vo);
        // ????????????
        String[] arrs = sysUser.getServerNos().split(",");
        vo.setServerNoList(Arrays.asList(arrs));
        // ?????????????????????
        List<ClientRoleBo> clientRoles = sysUserRoleMapper.selectClientRoleByUserId(id);
        vo.setClientRoles(clientRoles);
        return vo;
    }

    /**
     * ??????????????????
     * @param dto
     * @return
     */
    @Override
    public PageInfo<SysUserListVo> getUserList(SysUserListDTO dto) {
        PageHelper.startPage(dto.getCurrent(), dto.getPageSize());
        List<SysUserListVo> list = sysUserMapper.selectListByDto(dto);
        //????????????????????????  ????????????????????????
        list.stream().forEach(vo ->  vo.setAddress(regionalismService.getNameByCode(vo.getProvince())
                + regionalismService.getNameByCode(vo.getCity()) + regionalismService.getNameByCode(vo.getArea()))
                .setUpdateDateStr(DateTools.dateToString(vo.getUpdateDatetime(), DateTools.DF_MINUTE)));
        PageInfo<SysUserListVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * ????????????
     * @param id
     */
    @Override
    public void deleteUserById(Long id) throws Exception {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        sysUserMapper.deleteUserById(id);
        // ?????????????????????
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

    /**
     * ?????????????????????????????????
     * @param regionalismCode
     * @return
     */
    @Override
    public List<ProvinceSysUserVo> findProvinceUserList(String regionalismCode) {
        List<ProvinceSysUserVo> vos = sysUserMapper.selectProvinceUserList(regionalismCode);
        return vos;
    }

    private void cleanJwtRedis (String username) {
        //??????redis??????token
        valueOperations.set(RedisConstant.REDIS_LOGIN + username, "", 1, TimeUnit.MICROSECONDS);
    }
}
