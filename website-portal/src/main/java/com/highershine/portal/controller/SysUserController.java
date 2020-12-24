package com.highershine.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.highershine.portal.common.constants.RedisConstant;
import com.highershine.portal.common.entity.dto.SysUserDTO;
import com.highershine.portal.common.entity.dto.SysUserListDTO;
import com.highershine.portal.common.entity.dto.TokenDTO;
import com.highershine.portal.common.entity.dto.UpdatePasswordDTO;
import com.highershine.portal.common.entity.po.SysUser;
import com.highershine.portal.common.entity.vo.FindSysUserVo;
import com.highershine.portal.common.entity.vo.SysUserListVo;
import com.highershine.portal.common.entity.vo.SysUserVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.exception.RegisterException;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    @ApiOperation("获取token(薛博仁)")
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

    @PostMapping("password/update")
    @ApiOperation("修改密码(薛博仁)")
    public Result updatePassword(@RequestBody @Valid UpdatePasswordDTO dto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String message = bindingResult.getFieldError().getDefaultMessage();
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
            }
            if (!sysUserService.validSrcPassword(dto)) {
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), "原始密码错误");
            }
            sysUserService.updatePassword(dto);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【用户管理】修改密码异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    @GetMapping("password/reset/{id}")
    @ApiOperation("密码重置(薛博仁)")
    @Secured("admin")
    public Result resetPassword(@PathVariable("id") Long id) {
        try {
            sysUserService.resetPassword(id);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【用户管理】密码重置异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping(value={"userInfo", "userInfo/admin"})
    @ApiOperation("获取用户信息接口(薛博仁)")
    public Result<SysUserVo> queryUserInfo() {
        try {
            SysUserVo vo = sysUserUtil.getSysUserVo();
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【用户管理】查询用户信息异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }


    /**
     * 注册用户
     * @return
     */
    @PostMapping("register")
    @ApiOperation("注册或新增用户接口(薛博仁)")
    public Result register(@Valid @RequestBody SysUserDTO dto, BindingResult bindingResult) {
        try {
            String message = "";
            if (bindingResult.hasErrors()) {
                message = bindingResult.getFieldError().getDefaultMessage();
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
            }
            //用户校验和注册
            SysUser sysUser = sysUserService.registerAndValid(dto);
            Map<String, Long> map = new HashMap<>();
            map.put("id", sysUser.getId());
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, map);
        } catch (RegisterException e) {
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("【用户管理】注册或新增用户接口异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 修改用户
     * @return
     */
    @PostMapping("update")
    @ApiOperation("修改用户接口(薛博仁)")
    public Result updateUser(@Valid @RequestBody SysUserDTO dto, BindingResult bindingResult) {
        try {
            String message = "";
            if (bindingResult.hasErrors()) {
                message = bindingResult.getFieldError().getDefaultMessage();
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), message);
            }
            if (dto.getId() == null) {
                return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), "用户id为空");
            }
            //修改用户信息
            sysUserService.updateUserAndValid(dto);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (RegisterException e) {
            return ResultUtil.errorResult(ExceptionEnum.ERROR_PARAMETERS.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("【用户管理】修改用户接口异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 查询用户信息
     * @return
     */
    @GetMapping("find/{id}")
    @ApiOperation("查询用户信息接口(薛博仁)")
    public Result<FindSysUserVo> findUserById(@PathVariable("id") Long id) {
        try {
            FindSysUserVo vo = sysUserService.findUserById(id);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【用户管理】查询用户信息接口异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 查询用户列表
     * @return
     */
    @PostMapping("list")
    @ApiOperation("查询用户列表接口(薛博仁)")
    public Result<PageInfo<SysUserListVo>> getUserList(@RequestBody(required = false) SysUserListDTO dto) {
        try {
            if (dto == null) {
                dto = new SysUserListDTO();
            }
            PageInfo<SysUserListVo> vo = sysUserService.getUserList(dto);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【用户管理】查询用户列表接口异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * 删除用户
     * @return
     */
    @GetMapping("delete/{id}")
    @ApiOperation("删除用户接口(薛博仁)")
    public Result deleteUserById(@PathVariable("id") Long id) {
        try {
            sysUserService.deleteUserById(id);
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS);
        } catch (Exception e) {
            log.error("【用户管理】删除用户接口异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }
}
