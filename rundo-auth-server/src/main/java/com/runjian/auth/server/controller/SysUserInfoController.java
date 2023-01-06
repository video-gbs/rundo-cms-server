package com.runjian.auth.server.controller;

import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.service.SysUserInfoService;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@RestController
@RequestMapping("/sysUserInfo")
public class SysUserInfoController {

    @Autowired
    private SysUserInfoService sysUserService;


    @PostMapping
    public CommonResponse addUser(SysUserInfo sysUserInfo){
        sysUserService.save(sysUserInfo);
        return CommonResponse.success();
    }


    @GetMapping
    public CommonResponse deleteUser(SysUserInfo sysUserInfo){
        sysUserService.removeById(sysUserInfo);
        return CommonResponse.success();
    }

    @GetMapping("/list")
    public CommonResponse getList() {
        List<SysUserInfo> list = sysUserService.list();
        return CommonResponse.success(list);
    }
}
