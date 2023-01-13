package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.domain.vo.SysUserInfoVO;
import com.runjian.auth.server.service.system.SysUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "用户管理")
@RestController
@RequestMapping("/sysUserInfo")
public class SysUserInfoController {

    @Autowired
    private SysUserInfoService sysUserService;

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public ResponseResult addUser(@RequestBody SysUserInfoDTO dto) {
        return sysUserService.addUser(dto);
    }

    @PostMapping("/update")
    public ResponseResult updateUser(@RequestBody SysUserInfoDTO dto) {
        return sysUserService.updateUser(dto);
    }

    @GetMapping("/getUser")
    @ApiOperation("获取用户详情")
    public ResponseResult<SysUserInfoVO> getUser(@RequestBody Long id) {
        return sysUserService.getUser(id);
    }

    @GetMapping("/getUserList")
    @ApiOperation("获取用户列表，无分页")
    public ResponseResult<List<SysUserInfoVO>> getUserList() {
        return sysUserService.getUserList();
    }
}
