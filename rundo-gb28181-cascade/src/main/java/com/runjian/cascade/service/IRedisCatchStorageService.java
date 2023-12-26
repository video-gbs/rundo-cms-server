package com.runjian.cascade.service;

import com.runjian.cascade.gb28181.bean.PlatformRegisterInfo;

/**
 * @author chenjialing
 */
public interface IRedisCatchStorageService {

    /**
     * 计数器。为cseq进行计数
     *
     * @return
     */
    Long getCSEQ();

    /**
     * 更新注册阶段信息
     * @param callId
     * @param platformRegisterInfo
     */
    void updatePlatformRegisterInfo(String callId, PlatformRegisterInfo platformRegisterInfo);

    /**
     * 查询注册阶段信息
     * @param callId
     * @return
     */
    PlatformRegisterInfo queryPlatformRegisterInfo(String callId);

    /**
     * 删除注册阶段信息
     * @param callId
     */
    void delPlatformRegisterInfo(String callId);

}
