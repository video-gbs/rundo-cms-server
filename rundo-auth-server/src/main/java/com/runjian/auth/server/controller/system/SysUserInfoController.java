package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysUserInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysUserInfoVO;
import com.runjian.auth.server.service.system.SysUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseResult<?> addUser(@RequestBody AddSysUserInfoDTO dto) {
        log.info("添加用户前端传参{}", JSONUtil.toJsonStr(dto));
        sysUserService.addUser(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑用户")
    public ResponseResult<?> updateUser(@RequestBody AddSysUserInfoDTO dto) {
        log.info("编辑用户前端传参{}", JSONUtil.toJsonStr(dto));
        sysUserService.updateUser(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/getUser")
    @ApiOperation("获取用户详情")
    public ResponseResult<SysUserInfoVO> getUser(@RequestParam Long id) {
        return sysUserService.getUser(id);
    }

    @GetMapping("/getUserList")
    @ApiOperation("获取用户列表，无分页")
    public ResponseResult<List<SysUserInfoVO>> getUserList() {
        return sysUserService.getUserList();
    }
}
