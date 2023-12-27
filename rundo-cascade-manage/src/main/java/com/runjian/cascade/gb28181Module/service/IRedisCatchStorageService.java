package com.runjian.cascade.gb28181Module.service;


import com.runjian.cascade.gb28181Module.gb28181.bean.PlatformRegisterInfo;
import com.runjian.cascade.gb28181Module.gb28181.bean.SipTransactionInfo;

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

    void updatePlatformRegisterSip(String platformGbId, SipTransactionInfo sipTransactionInfo);

    SipTransactionInfo queryPlatformRegisterSip(String platformGbId);

    void delPlatformRegisterSip(String platformGbId);

}
