package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.domain.dto.system.AddSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QueryEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysRoleInfoDTO;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import com.runjian.common.config.response.CommonResponse;
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
    public CommonResponse<?> addRole(@RequestBody AddSysRoleInfoDTO dto) {
        log.info("新建角色前端传参{}", JSONUtil.toJsonStr(dto));
        sysRoleInfoService.addRole(dto);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    @ApiOperation("编辑角色")
    public CommonResponse<?> updateRole(@RequestBody UpdateSysRoleInfoDTO dto) {
        sysRoleInfoService.updateRole(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除角色")
    public CommonResponse<?> remove(@PathVariable Long id) {
        sysRoleInfoService.removeById(id);
        return CommonResponse.success();
    }

    @PostMapping("/batchRemove")
    @ApiOperation("批量删除角色")
    public CommonResponse<?> batchRemove(@RequestBody List<Long> ids) {
        sysRoleInfoService.batchRemove(ids);
        return CommonResponse.success();
    }


    @PostMapping("/getListByPage")
    @ApiOperation("获取角色分页列表")
    public CommonResponse<IPage<SysRoleInfoVO>> getListByPage(@RequestBody QuerySysRoleInfoDTO dto) {
        return CommonResponse.success(sysRoleInfoService.getSysRoleInfoByPage(dto));
    }

    @PostMapping("/getEditUserSysRoleInfoList")
    @ApiOperation("新增编辑用户时获取角色分页列表")
    public CommonResponse<IPage<EditUserSysRoleInfoVO>> getEditUserSysRoleInfoList(@RequestBody QueryEditUserSysRoleInfoDTO dto) {
        return CommonResponse.success(sysRoleInfoService.getEditUserSysRoleInfoList(dto));
    }
}
