package com.runjian.auth.server.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.vo.SysOrgNode;
import com.runjian.auth.server.entity.system.SysOrg;
import com.runjian.auth.server.service.system.SysOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseResult<Boolean> save(@RequestBody SysOrg dto) {
        return new ResponseResult<>(200, "操作成功", sysOrgService.save(dto));
    }

    @PostMapping
    @ApiOperation("删除部门")
    public ResponseResult<Boolean> delet(@RequestBody SysOrg dto) {
        return new ResponseResult<>(200, "操作成功", sysOrgService.removeById(dto));
    }

    @PostMapping("/update")
    @ApiOperation("编辑部门")
    public ResponseResult<Boolean> updateSysDict(@RequestBody SysOrg dto) {
        return new ResponseResult<>(200, "操作成功", sysOrgService.updateById(dto));
    }

    @GetMapping("/getById")
    @ApiOperation("获取部门信息")
    public ResponseResult<SysOrg> getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysOrgService.getById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取部门列表")
    public ResponseResult<List<SysOrg>> getList() {
        return new ResponseResult<>(200, "操作成功", sysOrgService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取部门列表")
    public ResponseResult<IPage<SysOrg>> getListByPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysOrgService.getListByPage(pageNum, pageSize));
    }

    @GetMapping("/getSysOrgTreeById")
    @ApiOperation("获取组织机构树")
    public ResponseResult<List<SysOrgNode>> getSysOrgById(
            @Param("id") Long id,
            @Param("orgNameLike") String orgName) {
        return new ResponseResult<>(200, "操作成", sysOrgService.getSysOrgTree(id, orgName));
    }
}
