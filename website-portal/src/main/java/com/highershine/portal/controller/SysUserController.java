package com.highershine.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highershine.portal.common.constants.CommonConstant;
import com.highershine.portal.common.constants.RedisConstant;
import com.highershine.portal.common.constants.RedisTimeoutConstant;
import com.highershine.portal.common.entity.bo.SysUserBo;
import com.highershine.portal.common.entity.dto.LoginDTO;
import com.highershine.portal.common.entity.dto.TokenDTO;
import com.highershine.portal.common.entity.po.SysUserRole;
import com.highershine.portal.common.entity.vo.SysUserLoginVo;
import com.highershine.portal.common.entity.vo.SysUserVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.SysUserService;
import com.highershine.portal.common.utils.ResultUtil;
import com.highershine.portal.common.utils.SHA256Util;
import com.highershine.portal.common.utils.SysUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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

    // mapper 声明
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${manage.session.timeout}")
    private long timeout;


    @GetMapping("token")
    @ApiOperation("获取token")
    public Result submitLogin(String code) throws Exception {
        RequestEntity httpEntity = new RequestEntity<>(getHttpBody(code), getHttpHeaders(),
                HttpMethod.POST, URI.create("http://192.168.10.182:8080/oauth/token"));
        ResponseEntity<TokenDTO> exchange = restTemplate.exchange(httpEntity, TokenDTO.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, exchange.getBody());
        }
        throw new RuntimeException("请求令牌失败！");
    }

    private MultiValueMap<String, String> getHttpBody(String code) throws UnsupportedEncodingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "http://192.168.10.182:8008/web-portal/su/token");
        params.add("scope", "all");
        return params;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("website", "2020");
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    @PostMapping("register")
    @ApiOperation("用户注册接口(待开发)")
    public Result submitLogin() {
        // 与mongoDB加密保持一致
        String password = "123456";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //encodeResult 为需要存入数据中加盐加密后的密码
        String encodeResult = bCryptPasswordEncoder.encode(password);
        //登录时 将用户输入的密码 与 数据库中存储的密码 做校验
        if (BCrypt.checkpw(password, encodeResult)) {
            log.info("校验通过");
        } else {
            log.info("密码错误");
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
    }

    /**
     * 用户登录
     *
     * @param dto
     * @param bindingResult
     * @return
     */
    @PostMapping("login")
    @ApiOperation("系统登录接口(薛博仁)")
    public Result<SysUserLoginVo> submitLogin(@Valid @RequestBody LoginDTO dto,
                                              BindingResult bindingResult, HttpServletResponse response) {
        // 参数出现异常, 校验参数必填
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            log.error("【用户登录】用户登录参数异常， 异常信息：{}", message);
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
        }
        SysUserLoginVo sysUserLoginVo = new SysUserLoginVo();
        try {
           // 查询用户bo对象
            SysUserBo sysUserBo = sysUserService.selectByUsername(dto.getUsername());
            if (sysUserBo != null && BCrypt.checkpw(dto.getPassword(), sysUserBo.getSysUser().getPassword())) {
                String userStr = mapper.writeValueAsString(sysUserBo);
                // 获取sessionId
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();
                String sessionId = SHA256Util.getSHA256(request.getSession().getId());
                // 缓存用户登录信息
                valueOperations.set(RedisConstant.REDIS_LOGIN + sessionId, userStr, RedisTimeoutConstant.TIMEOUT_HOURS, TimeUnit.HOURS);
                valueOperations.set(RedisConstant.REDIS_LOGIN + sessionId, userStr,
                        timeout == 0 ? RedisTimeoutConstant.TIMEOUT_HOURS : timeout, TimeUnit.HOURS);

                sysUserLoginVo.setId(sysUserBo.getSysUser().getId());
                sysUserLoginVo.setJsessionid(sessionId);
                sysUserLoginVo.setResult(true);
                // 设置cookie
                Cookie cookie = new Cookie(CommonConstant.JSESSIONID, sessionId);
                cookie.setMaxAge(60 * 60 * 8);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            } else {
                sysUserLoginVo.setResult(false);
            }
        } catch (Exception e) {
            log.error("【用户登录】用户登录异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, sysUserLoginVo);
    }

    /**
     * 系统登出接口异常
     * @return
     */
    @GetMapping("logout")
    @ApiOperation("系统登出接口(薛博仁)")
    public Result logout() {
        try {
            valueOperations.set(RedisConstant.REDIS_LOGIN
                    + sysUserUtil.getJessionId(), "", 1, TimeUnit.MICROSECONDS);
        } catch (Exception e) {
            log.error("【用户登录】系统登出接口异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
    }


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
            SysUserBo sysUserBo = sysUserUtil.getSysUserByRedis();
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(sysUserBo.getSysUser(), sysUserVo);
            sysUserVo.setUserRole(sysUserBo.getSysUserRoleList().stream().map(SysUserRole::getRoleId).toArray(String[]::new));
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, sysUserVo);
        } catch (IOException e) {
            log.error("【用户查询】查询用户信息异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
