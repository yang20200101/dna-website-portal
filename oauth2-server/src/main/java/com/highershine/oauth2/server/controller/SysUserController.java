package com.highershine.oauth2.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.highershine.oauth2.server.entity.Result;
import com.highershine.oauth2.server.entity.SysUser;
import com.highershine.oauth2.server.enums.ResultEnum;
import com.highershine.oauth2.server.utils.JwtUtils;
import com.highershine.oauth2.server.utils.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: mizhanlei
 * @Date: 2020/12/1 7:56
 */
@RestController
public class SysUserController {

    @RequestMapping("/user/userInfo")
    public Result<SysUser> getUserInfo(HttpServletRequest request) throws Exception {
        String token = request.getHeader(JwtUtils.HEADER_TOKEN_NAME);
        String tokenBody = JwtUtils.testJwt(token);
        JSONObject user = JSON.parseObject(tokenBody).getJSONObject("user");
        SysUser sysUser = JSON.toJavaObject(user,SysUser.class);
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, sysUser);
    }


    /** 不受保护的资源 */
    @GetMapping("/save/no")
    public String noSave(){
        return "no save";
    }
}
