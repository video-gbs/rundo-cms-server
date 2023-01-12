package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.entity.system.SysMenuInfo;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // @PostMapping("/add")
    // @ApiOperation("添加菜单")
    // public ResponseResult addSysMenu(@RequestBody SysMenuInfoDTO dto) {
    //     return sysMenuInfoService.addSysMenu(dto);
    // }
    @PostMapping("/add")
    @ApiOperation("添加菜单")
    public ResponseResult save(@RequestBody SysMenuInfo dto) {
        sysMenuInfoService.save(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    // @PostMapping("/update")
    // @ApiOperation("编辑菜单")
    // public ResponseResult updateSysMenu(@RequestBody SysMenuInfoDTO dto) {
    //     return sysMenuInfoService.updateSysMenu(dto);
    // }

    @PostMapping("/update")
    @ApiOperation("编辑菜单")
    public ResponseResult updateSysDict(@RequestBody SysMenuInfo dto) {
        sysMenuInfoService.updateById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取菜单信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysMenuInfoService.getById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取菜单列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", sysMenuInfoService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取菜单列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysMenuInfoService.list());
    }

    // @GetMapping("/sysMenuInfoList")
    // @ApiOperation("获取菜单列表")
    // public ResponseResult<List<SysMenuInfoVO>> sysMenuInfoList() {
    //     return sysMenuInfoService.sysMenuInfoList();
    // }

}
