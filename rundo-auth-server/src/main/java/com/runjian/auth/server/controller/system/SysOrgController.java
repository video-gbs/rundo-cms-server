package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysOrgDTO;
import com.runjian.auth.server.domain.entity.system.SysOrg;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
import com.runjian.auth.server.service.system.SysOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/sysOrg")
public class SysOrgController {

    @Autowired
    private SysOrgService sysOrgService;

    @PostMapping("/add")
    @ApiOperation("添加部门")
    public ResponseResult<Boolean> save(@RequestBody AddSysOrgDTO dto) {
        log.info("添加部门前端传参信息{}", JSONUtil.toJsonStr(dto));
        sysOrgService.saveSysOrg(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除部门")
    public ResponseResult<String> delete(@PathVariable Long id) {
        return new ResponseResult<>(200, "操作成功", sysOrgService.removeSysOrgById(id));
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除部门")
    public ResponseResult<Boolean> batchDelete(@RequestBody List<SysOrg> list) {
        // TODO 级联判断
        return new ResponseResult<>(200, "操作成功", sysOrgService.removeBatchByIds(list));
    }

    // @PostMapping("/move")
    // @ApiOperation("移动部门")
    // public ResponseResult<Boolean> move(@RequestBody SysOrg dto) {
    //     return new ResponseResult<>(200, "操作成功", sysOrgService.updateById(dto));
    // }

    @PostMapping("/update")
    @ApiOperation("编辑部门信息")
    public ResponseResult<Boolean> update(@RequestBody UpdateSysOrgDTO dto) {
        log.info("编辑部门信息前端传参信息{}", JSONUtil.toJsonStr(dto));
        sysOrgService.updateSysOrgById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/tree")
    @ApiOperation("获取组织机构层级树")
    public ResponseResult<List<SysOrgTree>> getSysOrgById() {
        return new ResponseResult<>(200, "操作成", sysOrgService.getSysOrgTree());
    }

    @GetMapping("/getById")
    @ApiOperation("获取部门信息")
    public ResponseResult<SysOrg> getById(@RequestParam Long id) {
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


}
