package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysAppInfoDTO;
import com.runjian.auth.server.service.system.SysAppInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 应用信息 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "应用管理")
@RestController
@RequestMapping("/sysAppInfo")
public class SysAppInfoController {
    @Autowired
    SysAppInfoService sysAppInfoService;

    @PostMapping("/addSysAppInfo")
    @ApiOperation("添加应用")
    public ResponseResult addSysAppInfo(SysAppInfoDTO dto) {
        return sysAppInfoService.addSysAppInfo(dto);
    }


    //
    // @PostMapping("/addSysAppInfo")
    // @ApiOperation("编辑应用")
    // public CommonResponse updateSysAppInfo(SysAppInfo sysAppInfo) {
    //     return CommonResponse.success(sysAppInfoService.updateById(sysAppInfo));
    // }
}
