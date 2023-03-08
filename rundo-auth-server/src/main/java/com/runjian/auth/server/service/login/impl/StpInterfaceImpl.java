package com.runjian.auth.server.service.login.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.runjian.auth.server.mapper.RoleInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName StpInterfaceImpl
 * @Description 权限验证接口
 * @date 2023-03-01 周三 16:43
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private RoleInfoMapper roleInfoMapper;

    /**
     * 返回此 loginId 拥有的权限码列表(权限与角色可分开校验)
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 在判断权限时，会调用该方法
        List<String> list = new ArrayList<>();


        return roleInfoMapper.selectRoleCodeByUserId(StpUtil.getLoginIdAsLong());
    }

    /**
     * 返回此 loginId 拥有的角色权限列表(权限与角色可分开校验)
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.valueOf(loginId.toString());
        return roleInfoMapper.selectRoleCodeByUserId(userId);
    }
}
