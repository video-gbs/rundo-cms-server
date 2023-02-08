package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysMenuInfoTree;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@Slf4j
@RestController
@RequestMapping("/sysMenuInfo")
public class SysMenuInfoController {

    @Autowired
    private SysMenuInfoService sysMenuInfoService;

    @PostMapping("/tree")
    @ApiOperation("获取菜单层级树")
    public CommonResponse<List<SysMenuInfoTree>> getSysMenuTree(@RequestBody QuerySysMenuInfoDTO dto) {
        return CommonResponse.success(sysMenuInfoService.getSysMenuTree(dto));
    }


    @PostMapping("/add")
    @ApiOperation("添加菜单")
    public CommonResponse<?> addSysMenu(@RequestBody @Valid AddSysMenuInfoDTO dto) {
        log.info("添加菜单前端传参信息{}", JSONUtil.toJsonStr(dto));
        sysMenuInfoService.addSysMenu(dto);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    @ApiOperation("编辑菜单")
    public CommonResponse<?> updateSysMenu(@RequestBody UpdateSysMenuInfoDTO dto) {
        log.info("编辑菜单前端传参信息{}", JSONUtil.toJsonStr(dto));
        sysMenuInfoService.updateSysMenuInfoById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除菜单")
    public CommonResponse<String> removeSysMenu(@PathVariable Long id) {
        log.info("删除菜单前端传参信息{}", id);
        sysMenuInfoService.removeSysMenuInfoById(id);
        return CommonResponse.success();
    }



    @GetMapping("/getById")
    @ApiOperation("通过ID获取菜单详情")
    public CommonResponse<SysMenuInfoVO> getById(@RequestParam Long id) {
        return CommonResponse.success(sysMenuInfoService.getSysMenuInfoById(id));
    }


    @GetMapping("/getList")
    @ApiOperation("获取菜单列表 无分页")
    public CommonResponse<List<SysMenuInfoVO>> getList() {
        return CommonResponse.success(sysMenuInfoService.getSysMenuInfoList());
    }


}
