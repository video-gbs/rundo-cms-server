package com.runjian.auth.server.controller;

import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.service.SysUserInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/sysUserInfo")
public class SysUserInfoController {

    @Autowired
    private SysUserInfoService sysUserService;


    @PostMapping("add")
    @ApiOperation("添加用户")
    @PreAuthorize("hasAuthority('addUser')")
    public CommonResponse addUser(@RequestBody SysUserInfo sysUserInfo) {
        sysUserService.save(sysUserInfo);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    public CommonResponse updateUser(@RequestBody SysUserInfo sysUserInfo) {
        sysUserService.updateById(sysUserInfo);
        return CommonResponse.success();
    }

    @GetMapping("/get")
    public CommonResponse getUser(@RequestBody SysUserInfo sysUserInfo) {
        sysUserService.getById(sysUserInfo.getId());

        return CommonResponse.success();
    }


    @ApiOperation("删除用户")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('deleteUser')")
    public CommonResponse deleteUser(@RequestBody SysUserInfo sysUserInfo) {
        sysUserService.removeById(sysUserInfo);
        return CommonResponse.success();
    }

    @ApiOperation("批量删除用户")
    @PostMapping("/batchDelete")
    public CommonResponse batchDelete(@RequestBody List<SysUserInfo> sysUserInfos) {
        sysUserService.removeBatchByIds(sysUserInfos);
        return CommonResponse.success();
    }

    @ApiOperation("查询所有用户")
    @GetMapping("/list")
    public CommonResponse getList() {
        return CommonResponse.success(sysUserService.list());
    }
}
