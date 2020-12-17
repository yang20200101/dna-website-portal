package com.highershine.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.constants.RedisConstant;
import com.highershine.portal.common.entity.dto.TokenDTO;
import com.highershine.portal.common.entity.vo.SysUserVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.SysUserService;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.common.utils.SysUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 用户接口
 * @Author: mizhanlei
 * @Date: 2019/11/27 14:44
 */
@Slf4j
@RestController
@RequestMapping("su")
@Api(description = "系统用户相关接口")
public class SysUserController {
    @Autowired
    private ValueOperations valueOperations;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserUtil sysUserUtil;
    @Value("${oauth2.server.tokenAddr}")
    private String tokenAddr;
    @Value("${oauth2.server.clientId}")
    private String clientId;
    @Value("${oauth2.server.clientSecret}")
    private String clientSecret;

    // mapper 声明
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("token")
    @ApiOperation("获取token")
    public Result submitLogin(String code) throws Exception {
        RequestEntity httpEntity = new RequestEntity<>(getHttpBody(code), getHttpHeaders(),
                HttpMethod.POST, URI.create(tokenAddr));
        ResponseEntity<TokenDTO> exchange = restTemplate.exchange(httpEntity, TokenDTO.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            // redis管理jwtToken失效
            TokenDTO tokenDTO = exchange.getBody();
            String accessToken = tokenDTO.getAccessToken();
            SysUserVo user = sysUserUtil.getSysUserVoByToken(accessToken);
            String expiresIn = tokenDTO.getExpiresIn();
            valueOperations.set(RedisConstant.REDIS_LOGIN + user.getUsername(), accessToken, Long.parseLong(expiresIn), TimeUnit.SECONDS);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, exchange.getBody());
        }
        throw new RuntimeException("请求令牌失败！");
    }

    private MultiValueMap<String, String> getHttpBody(String code) throws UnsupportedEncodingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        String redirectUri = sysUserService.selectOauthRedirectUri(clientId);
        params.add("redirect_uri", redirectUri);
        params.add("scope", "all");
        return params;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(clientId, clientSecret);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

//    @PostMapping("password/update")
//    @ApiOperation("修改密码(待开发)")
//    public Result updatePassword() {
//        // 与mongoDB加密保持一致
//        String password = "123456";
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        //encodeResult 为需要存入数据中加盐加密后的密码
//        String encodeResult = bCryptPasswordEncoder.encode(password);
//        //登录时 将用户输入的密码 与 数据库中存储的密码 做校验
//        if (BCrypt.checkpw(password, encodeResult)) {
//            log.info("校验通过");
//        } else {
//            log.info("密码错误");
//        }
//        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
//    }
//
//    @GetMapping("password/reset/{id}")
//    @ApiOperation("密码重置")
//    public Result resetPassword(@PathVariable("id") Long id) {
//        try {
//            sysUserService.resetPassword(id);
//            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
//        } catch (Exception e) {
//            log.error("【用户管理】密码重置异常， 异常信息：{}", e);
//            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
//        }
//    }


    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @GetMapping(value={"userInfo", "userInfo/admin"})
    @ApiOperation("获取用户信息接口(薛博仁)")
    public Result<SysUserVo> queryUserInfo(HttpServletRequest request) {
        try {
            SysUserVo vo = sysUserUtil.getSysUserVo();
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【用户管理】查询用户信息异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
