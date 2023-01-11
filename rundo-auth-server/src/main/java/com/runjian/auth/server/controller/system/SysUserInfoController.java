package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.entity.system.SysUserInfo;
import com.runjian.auth.server.service.system.SysUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/addUser")
    @ApiOperation("添加用户")
    public ResponseResult addUser(@RequestBody SysUserInfoDTO dto) {
        return sysUserService.addUser(dto);
    }

    @PostMapping("/updateUser")
    public ResponseResult updateUser(@RequestBody SysUserInfo sysUserInfo) {
        return sysUserService.updateUser();
    }
}
