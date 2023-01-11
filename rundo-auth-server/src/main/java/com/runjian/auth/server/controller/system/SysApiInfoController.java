package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysApiInfoDTO;
import com.runjian.auth.server.service.system.SysApiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 接口信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "接口管理")
@RestController
@RequestMapping("/sysApiInfo")
public class SysApiInfoController {

    @Autowired
    private SysApiInfoService sysApiInfoService;

    @PostMapping("/addSysApi")
    @ApiOperation("添加接口")
    public ResponseResult addSysApi(@RequestBody SysApiInfoDTO dto) {
        return sysApiInfoService.addSysApi(dto);
    }
    @PostMapping("/updateSysApi")
    @ApiOperation("编辑接口")
    public ResponseResult updateSysApi(@RequestBody SysApiInfoDTO dto) {
        return sysApiInfoService.updateSysApi(dto);
    }
}
