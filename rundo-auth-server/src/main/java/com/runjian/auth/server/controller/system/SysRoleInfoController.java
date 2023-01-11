package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysRoleInfoDTO;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/sysRoleInfo")
public class SysRoleInfoController {

    @Autowired
    private SysRoleInfoService sysRoleInfoService;

    @PostMapping("/addRole")
    @ApiOperation("添加角色")
    public ResponseResult addRole(@RequestBody SysRoleInfoDTO dto) {
        return sysRoleInfoService.addRole(dto);
    }
}
