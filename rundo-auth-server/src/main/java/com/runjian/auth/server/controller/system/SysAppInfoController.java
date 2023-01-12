package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.entity.system.SysAppInfo;
import com.runjian.auth.server.service.system.SysAppInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    @ApiOperation("添加应用")
    public ResponseResult save(@RequestBody SysAppInfo dto) {
        sysAppInfoService.save(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑应用")
    public ResponseResult updateSysDict(@RequestBody SysAppInfo dto) {
        sysAppInfoService.updateById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取应用信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysAppInfoService.getById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取应用列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", sysAppInfoService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取应用列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysAppInfoService.list());
    }


}
