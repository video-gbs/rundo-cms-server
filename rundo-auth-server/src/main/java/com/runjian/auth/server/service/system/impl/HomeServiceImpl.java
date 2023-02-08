package com.runjian.auth.server.service.system.impl;

import com.runjian.auth.server.domain.entity.system.SysUserInfo;
import com.runjian.auth.server.service.system.HomeSevice;
import com.runjian.auth.server.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HomeServiceImpl
 * @Description 门户首页
 * @date 2023-02-08 周三 11:53
 */
@Service
public class HomeServiceImpl implements HomeSevice {

    @Autowired
    private UserUtils userUtils;

    @Override
    public void getIndex() {
        SysUserInfo sysUserInfo = userUtils.getSysUserInfo();
        Long userId = sysUserInfo.getId();
        // 通过登录的用户查取该用户已授权的应用信息


    }
}
