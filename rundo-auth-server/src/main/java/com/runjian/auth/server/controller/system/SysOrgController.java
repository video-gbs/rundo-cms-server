package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.vo.SysOrgVO;
import com.runjian.auth.server.entity.system.SysOrg;
import com.runjian.auth.server.service.system.SysOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 组织机构表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/sysOrg")
public class SysOrgController {

    @Autowired
    private SysOrgService sysOrgService;

    @PostMapping("/add")
    @ApiOperation("添加部门")
    public ResponseResult save(@RequestBody SysOrg dto) {
        sysOrgService.save(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑部门")
    public ResponseResult updateSysDict(@RequestBody SysOrg dto) {
        sysOrgService.updateById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取部门信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysOrgService.getById(id));
    }
    @GetMapping("getSysOrgById")
    @ApiOperation("查询部门")
    public ResponseResult<SysOrgVO> getSysOrgById(Long id) {
        return new ResponseResult<>(200, "操作成", sysOrgService.getSysOrgById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取部门列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", sysOrgService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取部门列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysOrgService.list());
    }
}
