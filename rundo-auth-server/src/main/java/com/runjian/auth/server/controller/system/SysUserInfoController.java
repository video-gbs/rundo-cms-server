package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.vo.system.EditSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.ListSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.service.system.SysUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "用户管理")
@Slf4j
@RestController
@RequestMapping("/sysUserInfo")
public class SysUserInfoController {

    @Autowired
    private SysUserInfoService sysUserService;

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public ResponseResult<?> addUser(@RequestBody @Valid AddSysUserInfoDTO dto) {
        log.info("添加用户前端传参{}", JSONUtil.toJsonStr(dto));
        if (Objects.equals(dto.getPassword(), dto.getPassword())) {
            sysUserService.saveSysUserInfo(dto);
        } else {
            return new ResponseResult<>(200, "两次密码不一致");
        }
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("编辑时回显用户信息")
    public ResponseResult<EditSysUserInfoVO> getUserById(@PathVariable Long id) {
        return new ResponseResult<>(200, "操作成功", sysUserService.getSysUserInfoById(id));
    }

    @PostMapping("/update")
    @ApiOperation("编辑用户")
    public ResponseResult<?> updateUser(@RequestBody UpdateSysUserInfoDTO dto) {
        log.info("编辑用户前端传参{}", JSONUtil.toJsonStr(dto));
        if (Objects.equals(dto.getPassword(), dto.getPassword())) {
            sysUserService.updateSysUserInfo(dto);
        } else {
            return new ResponseResult<>(200, "两次密码不一致");
        }
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/status/change")
    @ApiOperation("用户状态切换")
    public ResponseResult<?> changeStatus(@RequestBody StatusSysUserInfoDTO dto) {
        log.info("应用状态切换前端传参{}", JSONUtil.toJsonStr(dto));
        sysUserService.changeStatus(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除用户")
    public ResponseResult<?> removeById(@PathVariable Long id) {
        sysUserService.removeSysUserInfoById(id);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/batchRemove")
    @ApiOperation("删除用户")
    public ResponseResult<?> removeById(@RequestBody List<Long>  ids) {
        sysUserService.batchRemoveSysUserInfo(ids);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/getListByPage")
    @ApiOperation("获取用户列表")
    public ResponseResult<IPage<ListSysUserInfoVO>> getSysUserInfoByPage(@RequestBody QuerySysUserInfoDTO dto) {
        return new ResponseResult<>(200, "操作成功", sysUserService.getSysUserInfoByPage(dto));
    }

    @PostMapping("/getRelationSysUserInfo/{id}")
    @ApiOperation("关联用户查看用户下信息")
    public ResponseResult<RelationSysUserInfoVO> getRelationSysUserInfoById(@PathVariable Long id) {
        return new ResponseResult<>(200, "操作成功", sysUserService.getRelationSysUserInfoById(id));
    }

    @PostMapping("/getRelationSysUserInfoList")
    @ApiOperation("关联用户用户列表")
    public ResponseResult<IPage<RelationSysUserInfoVO>> getRelationSysUserInfoList(@RequestBody QueryRelationSysUserInfoDTO dto) {
        return new ResponseResult<>(200, "操作成功", sysUserService.getRelationSysUserInfoList(dto));
    }


}
