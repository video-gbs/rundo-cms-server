package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.RoleDetailVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.domain.vo.tree.AppMenuApiTree;
import com.runjian.auth.server.domain.vo.tree.ConfigIdTree;
import com.runjian.auth.server.domain.vo.tree.DevopsIdTree;
import com.runjian.auth.server.service.system.RoleInfoService;
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
public class RoleInfoController {


    @Autowired
    private RoleInfoService roleInfoService;

    @PostMapping("/add")
    @ApiOperation("新建角色")
    public CommonResponse<?> addRole(@RequestBody AddSysRoleInfoDTO dto) {
        log.info("新建角色前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.save(dto);
        return CommonResponse.success();
    }

    @PostMapping("/getRoleDetail/{id}")
    @ApiOperation("编辑时回显角色详细信息")
    public CommonResponse<RoleDetailVO> getRoleDetailById(@PathVariable Long id){
        return CommonResponse.success(roleInfoService.getRoleDetailById(id));
    }


    @PostMapping("/update")
    @ApiOperation("编辑角色")
    public CommonResponse<?> updateRole(@RequestBody UpdateSysRoleInfoDTO dto) {
        roleInfoService.modifyById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除角色")
    public CommonResponse<?> remove(@PathVariable Long id) {
        roleInfoService.removeById(id);
        return CommonResponse.success();
    }

    @PostMapping("/batchRemove")
    @ApiOperation("批量删除角色")
    public CommonResponse<?> batchRemove(@RequestBody List<Long> ids) {
        roleInfoService.erasureBatch(ids);
        return CommonResponse.success();
    }

    @PostMapping("/status/change")
    @ApiOperation("接口状态切换")
    public CommonResponse<?> changeStatus(@RequestBody StatusSysRoleInfoDTO dto) {
        log.info("接口状态切换前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.modifyByStatus(dto);
        return CommonResponse.success();
    }


    @PostMapping("/getListByPage")
    @ApiOperation("获取角色分页列表")
    public CommonResponse<IPage<SysRoleInfoVO>> getListByPage(@RequestBody QuerySysRoleInfoDTO dto) {
        return CommonResponse.success(roleInfoService.findByPage(dto));
    }

    @PostMapping("/getEditUserSysRoleInfoList")
    @ApiOperation("新增编辑用户时获取角色分页列表")
    public CommonResponse<IPage<EditUserSysRoleInfoVO>> getEditUserSysRoleInfoList(@RequestBody QueryEditUserSysRoleInfoDTO dto) {
        return CommonResponse.success(roleInfoService.getEditUserSysRoleInfoList(dto));
    }

    @PostMapping("/getAppIdTree")
    @ApiOperation("新建角色时获取应用类相关ID列表")
    public CommonResponse<List<AppMenuApiTree>> getAppIdTree(){
        Integer appType = 1;
        // roleInfoService.getAppMenuApiTree(appType);

        return CommonResponse.success(roleInfoService.getAppMenuApiTree(appType));
    }

    @PostMapping("/getConfigIdTree")
    @ApiOperation("新建角色时获取配置类的相关ID的树")
    public CommonResponse<List<ConfigIdTree>> getConfigIdTree(){
        Integer appType = 2;
        roleInfoService.getAppMenuApiTree(appType);
        return null;
    }

    @PostMapping("/getDevopsIdTree")
    @ApiOperation("新建角色时获取运维类的相关ID的树")
    public CommonResponse<List<DevopsIdTree>> getDevopsIdTree(){
        Integer appType = 3;
        roleInfoService.getAppMenuApiTree(appType);
        return null;
    }
}
