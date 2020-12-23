package com.highershine.portal.controller;

import com.highershine.portal.common.entity.vo.SysRoleListVo;
import com.highershine.portal.common.enums.ExceptionEnum;
import com.highershine.portal.common.enums.ResultEnum;
import com.highershine.portal.common.result.Result;
import com.highershine.portal.common.service.SysRoleService;
import com.highershine.portal.common.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 角色接口
 * @Author: xueboren
 * @Date: 2020/12/23 14:44
 */
@Slf4j
@RestController
@RequestMapping("role")
@Api(description = "系统角色相关接口")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("list")
    @ApiOperation("获取所有角色")
    public Result getRoleList() {
        try {
            List<SysRoleListVo> vo = sysRoleService.getRoleList();
            return ResultUtil.successResult(ResultEnum.SUCCESS_STATUS, vo);
        } catch (Exception e) {
            log.error("【用户管理】修改密码异常， 异常信息：{}", e);
            return ResultUtil.errorResult(ExceptionEnum.UNKNOWN_EXCEPTION);
        }
    }

}
