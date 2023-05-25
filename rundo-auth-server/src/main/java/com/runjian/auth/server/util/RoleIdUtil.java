package com.runjian.auth.server.util;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.domain.entity.RoleInfo;
import com.runjian.auth.server.domain.entity.RoleOrg;
import com.runjian.auth.server.service.system.RoleInfoService;
import com.runjian.auth.server.service.system.RoleOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleIdUtil {

    @Lazy
    @Autowired
    private RoleInfoService roleInfoService;

    @Lazy
    @Autowired
    private RoleOrgService roleOrgService;

    public List<Long> getRoleIdList() {
        List<String> roleCodeList = StpUtil.getRoleList();
        LambdaQueryWrapper<RoleInfo> roleInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleInfoLambdaQueryWrapper.in(RoleInfo::getRoleCode, roleCodeList);
        return roleInfoService.list(roleInfoLambdaQueryWrapper).stream().map(RoleInfo::getId).collect(Collectors.toList());
    }

    public List<Long> getRoleOrgIdList(List<Long> roleIds){
        LambdaQueryWrapper<RoleOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleOrg::getRoleId, roleIds);
        return roleOrgService.list(queryWrapper).stream().map(RoleOrg::getOrgId).collect(Collectors.toList());
    }
}
