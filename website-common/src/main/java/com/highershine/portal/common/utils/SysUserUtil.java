package com.highershine.portal.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.constants.CommonConstant;
import com.highershine.portal.common.constants.RedisConstant;
import com.highershine.portal.common.entity.bo.SysUserBo;
import com.highershine.portal.common.entity.po.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 获取用户信息工具类
 * @Author: xueboren
 * @Date: 2019/12/26 10:28
 */
@Service("sysUserUtil")
public class SysUserUtil {
    @Autowired
    private ValueOperations valueOperations;
    @Autowired
    private HttpServletRequest request;
    // mapper 声明
    private final ObjectMapper mapper = new ObjectMapper();

    public SysUserBo getSysUserByRedis() throws IOException {
        String jsessionid = getJessionId();
        //获取用户权限
        SysUserBo sysUserBo = mapper.readValue(valueOperations.get(RedisConstant.REDIS_LOGIN + jsessionid).toString(), SysUserBo.class);
        return sysUserBo;
    }

    public String getNickName() throws IOException {
        return getSysUserByRedis().getSysUser().getNickname();
    }

    public List<String> getRoleList() throws IOException {
        List<String> roleList = new ArrayList<>();
        SysUserBo sysUserBo = getSysUserByRedis();
        List<SysUserRole> sysUserRoleList = sysUserBo.getSysUserRoleList();
        sysUserRoleList.stream().forEach(list -> roleList.add(list.getRoleId()));
        return roleList;
    }

    public String getJessionId() {
        String jsessionid = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (CommonConstant.JSESSIONID.equals(cookie.getName())) {
                jsessionid = cookie.getValue();
            }
        }
        return jsessionid;
    }
}
