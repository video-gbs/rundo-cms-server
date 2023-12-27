package com.runjian.cascade.service;

/**
 * @author Miracle
 * @date 2023/12/26 9:16
 */
public interface PlatformCascadeService {

    /**
     * 注册平台
     * @param platformId 平台id
     */
    void signInPlatform(Long platformId);

    /**
     * 登出平台
     * @param platformId 平台id
     */
    void logoutPlatform(Long platformId);

    /**
     * 数据订阅
     */
    void cascadeSubscribe(Long platformId, Boolean isSubscribe);

    /**
     * 订阅数据更新推送
     */
    void platformDataUpdate();

}
