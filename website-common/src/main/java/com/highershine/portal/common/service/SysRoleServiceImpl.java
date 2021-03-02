package com.highershine.portal.common.service;


import com.highershine.portal.common.constants.CommonConstant;
import com.highershine.portal.common.constants.RoleConstant;
import com.highershine.portal.common.entity.po.SysClient;
import com.highershine.portal.common.entity.po.SysRole;
import com.highershine.portal.common.entity.vo.SysRoleListVo;
import com.highershine.portal.common.entity.vo.SysRoleVo;
import com.highershine.portal.common.enums.HttpStatusEnum;
import com.highershine.portal.common.mapper.SysClientMapper;
import com.highershine.portal.common.mapper.SysRoleMapper;
import com.highershine.portal.common.utils.JSONUtil;
import com.highershine.portal.common.utils.SysUserUtil;
import com.highershine.portal.common.utils.URLConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author xueboren
 * @Date 2020/12/23 14:38
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysClientMapper sysClientMapper;
    @Autowired
    private SysUserUtil sysUserUtil;

    //缓存其他系统角色
    private Map<String, SysRoleListVo> cacheClientRoleMap = new HashMap<>();

    @PostConstruct
    public void init() throws Exception {
        List<SysClient> clientList = sysClientMapper.getClientList();
        for (SysClient sysClient : clientList) {
            SysRoleListVo vo = new SysRoleListVo();
            vo.setClientId(sysClient.getId()).setClientName(sysClient.getClientName());
            if (StringUtils.isNotBlank(sysClient.getRoleUrl())) {
                //其他系统角色
                String result = URLConnectionUtil.get(sysClient.getRoleUrl(), null);
                if (StringUtils.isBlank(result)) {
                    throw new RuntimeException("the url return is blank:" + sysClient.getRoleUrl());
                }
                Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
                Integer code = ((Long) resultMap.get("code")).intValue();
                if (!HttpStatusEnum.OK.getCode().equals(code)) {
                    throw new RuntimeException("the url return code is not success:" + sysClient.getRoleUrl());
                } else {
                    //请求成功
                    List<SysRoleVo> roleVos = (List) resultMap.get("data");
                    vo.setRoles(roleVos);
                    cacheClientRoleMap.put(sysClient.getId(), vo);
                }
            }
        }
    }

    @Override
    public List<SysRoleListVo> getRoleList() throws Exception {
        List<SysRoleListVo> voList = new ArrayList<>();
        List<SysRole> roleList = sysRoleMapper.getRoleList();
        List<SysClient> clientList = sysClientMapper.getClientList();
        for (SysClient sysClient : clientList) {
            SysRoleListVo vo = new SysRoleListVo();
            vo.setClientId(sysClient.getId()).setClientName(sysClient.getClientName());
            // 门户角色
            if (CommonConstant.CLIENT_WEBSITE.equals(sysClient.getId())) {
                List<SysRoleVo> roleVos = new ArrayList<>();
                for (SysRole sysRole : roleList) {
                    SysRoleVo sysRoleVo = new SysRoleVo();
                    sysRoleVo.setRoleId(sysRole.getId()).setRoleName(sysRole.getRoleName());
                    if (RoleConstant.ROLE_EXT_AVERAGE.equals(sysRole.getExtId())) {
                        sysRoleVo.setDefaultFlag("1");
                    } else {
                        if (RoleConstant.ROLE_EXT_ADMIN.equals(sysRole.getExtId())
                                || RoleConstant.ROLE_EXT_SECOND_ADMIN.equals(sysRole.getExtId())) {
                            //省级用户不允许操作的权限
                            sysRoleVo.setNotAllowedFlag("1");
                        } else {
                            sysRoleVo.setNotAllowedFlag("0");
                        }
                        sysRoleVo.setDefaultFlag("0");
                    }
                    roleVos.add(sysRoleVo);
                }
                vo.setRoles(roleVos);
                voList.add(vo);
            } else if (StringUtils.isNotBlank(sysClient.getRoleUrl())){
                //其他系统角色
                try {
                    String result = URLConnectionUtil.get(sysClient.getRoleUrl(),null);
                    if (StringUtils.isBlank(result)) {
                        throw new RuntimeException("the url return is blank:" + sysClient.getRoleUrl());
                    }
                    Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
                    Integer code = ((Long) resultMap.get("code")).intValue();
                    if (!HttpStatusEnum.OK.getCode().equals(code)) {
                        throw new RuntimeException("the url return code is not success:" + sysClient.getRoleUrl());
                    } else {
                        //请求成功
                        List<SysRoleVo> roleVos = (List) resultMap.get("data");
                        vo.setRoles(roleVos);
                        voList.add(vo);
                    }
                } catch (Exception e) {
                    //接口调用失败 从缓存中获取
                    voList.add(cacheClientRoleMap.get(sysClient.getId()));
                    log.error("【角色查询】获取其他系统角色异常， url：{}",  sysClient.getRoleUrl());
                }

            }
        }
        if (sysUserUtil.getSysUserVo().getUserRole().contains(RoleConstant.ROLE_EXT_ADMIN)) {
            for (SysRoleListVo vo : voList) {
                for (SysRoleVo role : vo.getRoles()) {
                    //管理员 允许修改任何系统的角色
                    role.setNotAllowedFlag("0");
                }
            }
        }
        return voList;
    }
}
