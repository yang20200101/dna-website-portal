package com.highershine.portal.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.entity.vo.SysUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 获取用户信息工具类
 * @Author: xueboren
 * @Date: 2019/12/26 10:28
 */
@Service("sysUserUtil")
public class SysUserUtil {
    @Autowired
    private HttpServletRequest request;
    // mapper 声明
    private final ObjectMapper mapper = new ObjectMapper();

    public SysUserVo getSysUserVo() throws Exception {
        SysUserVo vo = null;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(header)) {
            //token串
            String token = header.substring(header.lastIndexOf("bearer") + 8);
            vo = getSysUserVoByToken(token);
        }
        return vo;
    }

    public SysUserVo getSysUserVoByToken(String token) throws Exception {
        String tokenBody = JwtUtils.testJwt(token);
        //token串转对象
        JSONObject user = JSON.parseObject(tokenBody).getJSONObject("user");
        List<Map<String, String>> authorities = (List) user.get("authorities");
        //SysUserVo对象
        List<String> roles = authorities.stream().map(map -> map.get("authority")).collect(Collectors.toList());
        SysUserVo vo = JSON.toJavaObject(user, SysUserVo.class);
        vo.setUserRole(roles);
        return vo;
    }
}
