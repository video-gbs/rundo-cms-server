package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.entity.system.SysConfig;
import com.runjian.auth.server.service.system.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统全局参数配置 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "系统参数配置")
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;


    @PostMapping("/add")
    @ApiOperation("添加系统参数配置")
    public ResponseResult addUser(@RequestBody SysConfig dto) {
        return sysConfigService.addSysConfig(dto);
    }

    @PostMapping("/update")
    @ApiOperation("编辑系统参数配置")
    public ResponseResult updateUser(@RequestBody SysConfig dto) {
        return sysConfigService.updateSysConfig(dto);
    }

}
