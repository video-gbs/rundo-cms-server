package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysRoleInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "角色管理")
@Slf4j
@RestController
@RequestMapping("/sysRoleInfo")
public class SysRoleInfoController {

    @Autowired
    private SysRoleInfoService sysRoleInfoService;

    @PostMapping("/add")
    @ApiOperation("新建角色")
    public ResponseResult<?> addRole(@RequestBody AddSysRoleInfoDTO dto) {
        log.info("新建角色前端传参{}", JSONUtil.toJsonStr(dto));
        sysRoleInfoService.addRole(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑角色")
    public ResponseResult<?> updateRole(@RequestBody UpdateSysRoleInfoDTO dto) {
        sysRoleInfoService.updateRole(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除角色")
    public ResponseResult<?> remove(@PathVariable Long id) {
        sysRoleInfoService.removeById(id);
        return new ResponseResult<>(200, "操作成功");
    }

    public ResponseResult<?> batchRemove(@RequestBody List<Long> ids) {
        sysRoleInfoService.batchRemove(ids);
        return new ResponseResult<>(200, "操作成功");
    }


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
    public ResponseResult<IPage<SysRoleInfoVO>> getListByPage(@RequestBody QuerySysRoleInfoDTO dto) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysRoleInfoService.getSysRoleInfoByPage(dto));
    }
}
