package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.service.system.SysMenuInfoService;
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

    @PostMapping("/add")
    @ApiOperation("添加菜单")
    public ResponseResult<?> addSysMenu(@RequestBody @Valid AddSysMenuInfoDTO dto) {
        log.info("添加菜单前端传参信息{}", JSONUtil.toJsonStr(dto));
        sysMenuInfoService.addSysMenu(dto);
        return new ResponseResult<>(200, "新增菜单项成功!");
    }

    @PostMapping("/update")
    @ApiOperation("编辑菜单")
    public ResponseResult<?> updateSysMenu(@RequestBody UpdateSysMenuInfoDTO dto) {
        log.info("编辑菜单前端传参信息{}", JSONUtil.toJsonStr(dto));
        sysMenuInfoService.updateSysMenuInfoById(dto);
        return new ResponseResult<>(200, "更新菜单项成功!");
    }

    @PostMapping("/remove")
    @ApiOperation("删除菜单")
    public ResponseResult<?> removeSysMenu(@RequestParam Long id) {
        log.info("删除菜单前端传参信息{}", id);
        sysMenuInfoService.removeSysMenuInfoById(id);
        return new ResponseResult<>(200, "删除菜单项成功!");
    }

    @PostMapping("/tree")
    @ApiOperation("获取菜单层级树")
    public ResponseResult<?> getSysMenuTree() {

        return null;
    }



    @GetMapping("/getById")
    @ApiOperation("通过ID获取菜单详情")
    public ResponseResult<SysMenuInfoVO> getById(@RequestParam Long id) {
        return new ResponseResult<>(200, "操作成功", sysMenuInfoService.getSysMenuInfoById(id));
    }




    @GetMapping("/getList")
    @ApiOperation("获取菜单列表 无分页")
    public ResponseResult<List<SysMenuInfoVO>> getList() {
        return new ResponseResult<>(200, "操作成功", sysMenuInfoService.getSysMenuInfoList());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取菜单列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysMenuInfoService.list());
    }

}
