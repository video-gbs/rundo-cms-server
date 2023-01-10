package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.domain.dto.SysMenuInfoDTO;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sysMenuInfo")
public class SysMenuInfoController {

    @Autowired
    private SysMenuInfoService sysMenuInfoService;

    @PostMapping("/addSysMenu")
    @ApiOperation("添加接口")
    public CommonResponse addSysMenu(@RequestBody SysMenuInfoDTO dto) {
        return sysMenuInfoService.addSysMenu(dto);
    }
}
