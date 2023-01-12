package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysRoleInfoDTO;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    @ApiOperation("添加角色")
    public ResponseResult addRole(@RequestBody SysRoleInfoDTO dto) {
        return sysRoleInfoService.addRole(dto);
    }

    // @PostMapping("/add")
    // @ApiOperation("添加角色")
    // public ResponseResult save(@RequestBody SysRoleInfo dto) {
    //     sysRoleInfoService.save(dto);
    //     return new ResponseResult<>(200, "操作成功");
    // }

    @PostMapping("/update")
    @ApiOperation("编辑角色")
    public ResponseResult updateRole(@RequestBody SysRoleInfoDTO dto) {
        return sysRoleInfoService.updateRole(dto);
    }

    @PostMapping("/delete")
    @ApiOperation("删除角色")
    public ResponseResult deleteRole(@RequestBody Long id) {
        sysRoleInfoService.removeById(id);
        return new ResponseResult<>(200, "操作成功");
    }


    // @PostMapping("/update")
    // @ApiOperation("编辑角色")
    // public ResponseResult updateSysDict(@RequestBody SysRoleInfo dto) {
    //     sysRoleInfoService.updateById(dto);
    //     return new ResponseResult<>(200, "操作成功");
    // }

    @GetMapping("/getById")
    @ApiOperation("获取角色信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysRoleInfoService.getById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取角色列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", sysRoleInfoService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取角色列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysRoleInfoService.list());
    }
}
