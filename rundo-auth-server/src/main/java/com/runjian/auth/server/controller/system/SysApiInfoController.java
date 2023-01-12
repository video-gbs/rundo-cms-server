package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.entity.system.SysApiInfo;
import com.runjian.auth.server.service.system.SysApiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    @ApiOperation("添加接口")
    public ResponseResult save(@RequestBody SysApiInfo dto) {
        sysApiInfoService.save(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑接口")
    public ResponseResult updateSysDict(@RequestBody SysApiInfo dto) {
        sysApiInfoService.updateById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取接口信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysApiInfoService.getById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取接口列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", sysApiInfoService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取接口列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysApiInfoService.list());
    }
}
