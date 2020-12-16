package com.highershine.portal.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.entity.vo.SysUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
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
    private HttpServletRequest request1;
    // mapper 声明
    private final ObjectMapper mapper = new ObjectMapper();

    public SysUserVo getSysUserVo(HttpServletRequest request) throws Exception {
        SysUserVo vo = null;
        System.out.println("--------------");
        Enumeration<String> er = request.getHeaderNames();//获取请求头的所有name值
        while(er.hasMoreElements()) {
            String name = (String) er.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + "=" + value);
        }

        System.out.println("--------------");
        String header = request.getHeader(JwtUtils.HEADER_TOKEN_NAME);
        System.out.println("【getSysUserVo】header:" + header);
        if (StringUtils.isNotBlank(header)) {
            //token串
            String token = header.substring(header.lastIndexOf("bearer") + 7);
            String tokenBody = JwtUtils.testJwt(token);
            //token串转对象
            JSONObject user = JSON.parseObject(tokenBody).getJSONObject("user");
            List<Map<String, String>> authorities = (List) user.get("authorities");
            //SysUserVo对象
            List<String> roles = authorities.stream().map(map -> map.get("authority")).collect(Collectors.toList());
            vo = JSON.toJavaObject(user, SysUserVo.class);
            vo.setUserRole(roles);
        }
        return vo;
    }
}
