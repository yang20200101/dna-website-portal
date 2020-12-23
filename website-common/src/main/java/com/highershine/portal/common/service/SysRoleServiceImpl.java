package com.highershine.portal.common.service;


import com.highershine.portal.common.constants.RoleConstant;
import com.highershine.portal.common.entity.po.SysClient;
import com.highershine.portal.common.entity.po.SysRole;
import com.highershine.portal.common.entity.vo.SysRoleListVo;
import com.highershine.portal.common.entity.vo.SysRoleVo;
import com.highershine.portal.common.enums.HttpStatusEnum;
import com.highershine.portal.common.mapper.SysClientMapper;
import com.highershine.portal.common.mapper.SysRoleMapper;
import com.highershine.portal.common.utils.JSONUtil;
import com.highershine.portal.common.utils.URLConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Value("${oauth2.server.clientId}")
    private String clientId;

    @Override
    public List<SysRoleListVo> getRoleList() throws Exception {
        List<SysRoleListVo> voList = new ArrayList<>();
        List<SysRole> roleList = sysRoleMapper.getRoleList();
        List<SysClient> clientList = sysClientMapper.getClientList();
        for (SysClient sysClient : clientList) {
            SysRoleListVo vo = new SysRoleListVo();
            vo.setClientId(sysClient.getId()).setClientName(sysClient.getClientName());
            // 门户角色
            if (clientId.equals(sysClient.getId())) {
                List<SysRoleVo> roleVos = new ArrayList<>();
                for (SysRole sysRole : roleList) {
                    SysRoleVo sysRoleVo = new SysRoleVo();
                    sysRoleVo.setRoleId(sysRole.getId()).setRoleName(sysRole.getRoleName());
                    if (RoleConstant.ROLE_EXT_AVERAGE.equals(sysRole.getExtId())) {
                        sysRoleVo.setDefaultFlag("1");
                    } else {
                        sysRoleVo.setDefaultFlag("0");
                    }
                    roleVos.add(sysRoleVo);
                }
                vo.setRoles(roleVos);
                voList.add(vo);
            } else if (StringUtils.isNotBlank(sysClient.getRoleUrl())){
                //其他系统角色
                String result = URLConnectionUtil.get(sysClient.getRoleUrl(),null);
                if (StringUtils.isBlank(result)) {
                    throw new RuntimeException("the url return is blank:" + sysClient.getRoleUrl());
                }
                Map<String, Object> resultMap = JSONUtil.parseJsonToMap(result);
                Integer code = (Integer) resultMap.get("code");
                if (!HttpStatusEnum.OK.getCode().equals(code)) {
                    throw new RuntimeException("the url return code is not success:" + sysClient.getRoleUrl());
                } else {
                    //请求成功
                    List<SysRoleVo> roleVos = (List) resultMap.get("data");
                    vo.setRoles(roleVos);
                    voList.add(vo);
                }
            }
        }
        return voList;
    }
}
