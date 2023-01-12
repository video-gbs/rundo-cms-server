package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.entity.system.SysDict;
import com.runjian.auth.server.service.system.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "数据字典")
@RestController
@RequestMapping("/sysDict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @PostMapping("/add")
    @ApiOperation("添加数据字典")
    public ResponseResult save(@RequestBody SysDict dto) {
        sysDictService.save(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("修改数据字典")
    public ResponseResult updateSysDict(@RequestBody SysDict dto) {
        sysDictService.updateById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取数据字典信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysDictService.getById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取数据字典列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", sysDictService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取数据字典列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取数据字典列表
        return new ResponseResult<>(200, "操作成功", sysDictService.list());
    }
}
