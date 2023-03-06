package com.runjian.auth.gateway.component;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName StpInterfaceImpl
 * @Description 自定义权限验证接口扩展
 * @date 2023-03-01 周三 15:30
 */
public class StpInterfaceImpl implements StpInterface {


    /**
     *
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 先从Redis中获取缓存数据，获取不到时走RPC调用子服务 (专门的权限数据提供服务) 获取
        List<String> authList = (List<String>) StpUtil.getSession().get("authList");

        return null;
    }

    /**
     *
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 先从Redis中获取缓存数据，获取不到时走RPC调用子服务 (专门的权限数据提供服务) 获取

        return null;
    }

}
