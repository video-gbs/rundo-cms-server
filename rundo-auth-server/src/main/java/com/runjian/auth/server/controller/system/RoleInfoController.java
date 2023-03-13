package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.RoleDetailVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.domain.vo.tree.AppMenuApiTree;
import com.runjian.auth.server.service.system.RoleInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public CommonResponse<RoleDetailVO> getRoleDetailById(@PathVariable Long id) {
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

    @PostMapping("/relationUser/add")
    @ApiOperation("提交关联用户列表")
    public CommonResponse<?> addRelationUser(@RequestBody RoleRelationUserDTO dto) {
        log.info("提交关联用户列表前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.addRelationUser(dto);
        return CommonResponse.success();
    }

    @PostMapping("/getListByPage")
    @ApiOperation("获取角色分页列表")
    public CommonResponse<IPage<SysRoleInfoVO>> getListByPage(@RequestBody QuerySysRoleInfoDTO dto) {
        return CommonResponse.success(roleInfoService.findByPage(dto));
    }

    @PostMapping("/getEditUserSysRoleInfoList")
    @ApiOperation("新增与编辑用户时获取角色分页列表")
    public CommonResponse<IPage<EditUserSysRoleInfoVO>> getEditUserSysRoleInfoList(@RequestBody QueryEditUserSysRoleInfoDTO dto) {
        return CommonResponse.success(roleInfoService.getEditUserSysRoleInfoList(dto));
    }

    @PostMapping("/getAppMenuApiTree/{appType}")
    @ApiOperation("新建角色时获取应用类相关ID列表")
    @ApiImplicitParam(name = "appType", value = "应用分类 1 应用类，2 配置类，3 运维类", required = true)
    public CommonResponse<List<AppMenuApiTree>> getAppIdTree(@PathVariable Integer appType) {
        return CommonResponse.success(roleInfoService.getAppMenuApiTree(appType));
    }

    @PostMapping("/relationUser/right")
    @ApiOperation("右移从关联用户列表中移除用户")
    public CommonResponse<?> rightRelationUser(@RequestBody RoleRelationUserDTO dto) {
        log.info("右移从关联用户列表中移除用户，前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.rightRelationUser(dto);
        return CommonResponse.success();
    }

    @PostMapping("/relationUser/left")
    @ApiOperation("左移提交用户到已关联用户列表")
    public CommonResponse<?> leftRelationUser(@RequestBody RoleRelationUserDTO dto) {
        log.info("左移提交用户到已关联用户列表，前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.leftRelationUser(dto);
        return CommonResponse.success();
    }

    @PostMapping("/relationUserByRole")
    @ApiOperation("查询已关联用户列表")
    public CommonResponse<IPage<RelationSysUserInfoVO>> listRelationUser(@RequestBody @Valid QueryRoleRelationSysUserInfoDTO dto) {
        log.info("查询已关联用户列表传参{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.success(roleInfoService.listRelationUser(dto));
    }

}
