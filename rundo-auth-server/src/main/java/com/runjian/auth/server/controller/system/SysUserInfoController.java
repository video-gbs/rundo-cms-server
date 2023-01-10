package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/sysUserInfo")
public class SysUserInfoController {

    @Autowired
    private SysUserInfoService sysUserService;


    @PostMapping("add")
    @ApiOperation("添加用户")
    public CommonResponse addUser(@RequestBody SysUserInfoDTO dto) {
        return sysUserService.addUser(dto);
    }

    @PostMapping("/updateUser")
    public CommonResponse updateUser(@RequestBody SysUserInfo sysUserInfo) {
        return sysUserService.updateUser();
    }

    @GetMapping("/getUser")
    public CommonResponse getUser() {
        return sysUserService.getUser();
    }


    @ApiOperation("删除用户")
    @PreAuthorize("hasAuthority('deleteUser')")
    public CommonResponse deleteUser() {

        return sysUserService.deleteUser();
    }

    @ApiOperation("批量删除用户")
    @PostMapping("/batchDelete")
    public CommonResponse batchDeleteUsers() {
        return sysUserService.batchDeleteUsers();
    }

    @ApiOperation("查询所有用户")
    @GetMapping("/getUserList")
    public CommonResponse getUserList() {
        return sysUserService.getUserList();
    }

    @ApiOperation("分页查询所有用户")
    @GetMapping("/getUserListByPage")
    public CommonResponse getUserListByPage() {
        return sysUserService.getUserListByPage();
    }
}
