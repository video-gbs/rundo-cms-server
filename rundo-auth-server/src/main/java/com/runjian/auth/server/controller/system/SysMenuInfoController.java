package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysMenuInfoDTO;
import com.runjian.auth.server.domain.vo.SysMenuInfoVO;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/add")
    @ApiOperation("添加菜单")
    public ResponseResult addSysMenu(@RequestBody SysMenuInfoDTO dto) {
        return sysMenuInfoService.addSysMenu(dto);
    }

    @PostMapping("/update")
    @ApiOperation("编辑菜单")
    public ResponseResult updateSysMenu(@RequestBody SysMenuInfoDTO dto) {
        return sysMenuInfoService.updateSysMenu(dto);
    }

    @GetMapping("/sysMenuInfoList")
    @ApiOperation("获取菜单列表")
    public ResponseResult<List<SysMenuInfoVO>> sysMenuInfoList() {

        return sysMenuInfoService.sysMenuInfoList();
    }

}
