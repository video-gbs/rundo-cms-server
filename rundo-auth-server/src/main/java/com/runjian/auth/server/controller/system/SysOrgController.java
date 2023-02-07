package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.domain.dto.system.AddSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.MoveSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysOrgDTO;
import com.runjian.auth.server.domain.vo.system.SysOrgVO;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
import com.runjian.auth.server.service.system.SysOrgService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
    public CommonResponse<SysOrgVO> save(@RequestBody AddSysOrgDTO dto) {
        log.info("添加部门前端传参信息{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.create(200, "操作成功", sysOrgService.saveSysOrg(dto));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除部门")
    public CommonResponse<String> delete(@PathVariable Long id) {
        return CommonResponse.create(200, "操作成功", sysOrgService.removeSysOrgById(id));
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除部门")
    public CommonResponse<String> batchDelete(@RequestBody List<Long> ids) {
        return CommonResponse.create(200, "操作成功", sysOrgService.batchDelete(ids));
    }

    @PostMapping("/update")
    @ApiOperation("编辑部门信息")
    public CommonResponse<?> update(@RequestBody UpdateSysOrgDTO dto) {
        log.info("编辑部门信息前端传参信息{}", JSONUtil.toJsonStr(dto));
        sysOrgService.updateSysOrgById(dto);
        return CommonResponse.create(200, "操作成功", null);
    }

    @PostMapping("/move")
    @ApiOperation("移动部门")
    public CommonResponse<?> move(@RequestBody MoveSysOrgDTO dto) {
        sysOrgService.moveSysOrg(dto);
        return CommonResponse.create(200, "操作成功", null);
    }

    @GetMapping("/tree")
    @ApiOperation("获取组织机构层级树")
    public CommonResponse<List<SysOrgTree>> getSysOrgById() {
        return CommonResponse.create(200, "操作成", sysOrgService.getSysOrgTree());
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取部门信息")
    public CommonResponse<SysOrgVO> getById(@PathVariable Long id) {
        return CommonResponse.create(200, "操作成功", sysOrgService.getSysOrgById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取部门列表")
    public CommonResponse<List<SysOrgVO>> getList() {
        return CommonResponse.create(200, "操作成功", sysOrgService.getSysOrgList());
    }

    // @GetMapping("/getListByPage")
    // @ApiOperation("分页获取部门列表")
    // public CommonResponse<IPage<SysOrgVO>> getListByPage() {
    //     // TODO 分页获取应用列表
    //     return CommonResponse.create(200, "操作成功", sysOrgService.getListByPage(pageNum, pageSize));
    // }


}
