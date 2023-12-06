package com.runjian.cascade.gb28181.transmit.cmd;


import com.runjian.cascade.gb28181.bean.*;
import com.runjian.cascade.gb28181.event.SipSubscribe;
import gov.nist.javax.sip.message.SIPRequest;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.header.WWWAuthenticateHeader;
import java.text.ParseException;
import java.util.List;

public interface ISIPCommanderForPlatform {

    /**
     * 向上级平台注册
     * @param parentPlatform
     * @return
     */
    void register(ParentPlatform parentPlatform, SipSubscribe.Event errorEvent , SipSubscribe.Event okEvent) throws InvalidArgumentException, ParseException, SipException;

    void register(ParentPlatform parentPlatform, SipTransactionInfo sipTransactionInfo, SipSubscribe.Event errorEvent , SipSubscribe.Event okEvent) throws InvalidArgumentException, ParseException, SipException;


    void register(ParentPlatform parentPlatform, SipTransactionInfo sipTransactionInfo, WWWAuthenticateHeader www, SipSubscribe.Event errorEvent , SipSubscribe.Event okEvent, boolean isRegister) throws SipException, InvalidArgumentException, ParseException;

    /**
     * 向上级平台注销
     * @param parentPlatform
     * @return
     */
    void unregister(ParentPlatform parentPlatform, SipTransactionInfo sipTransactionInfo, SipSubscribe.Event errorEvent , SipSubscribe.Event okEvent) throws InvalidArgumentException, ParseException, SipException;


    /**
     * 向上级平发送心跳信息
     * @param parentPlatform
     * @return callId(作为接受回复的判定)
     */
    String keepalive(ParentPlatform parentPlatform,SipSubscribe.Event errorEvent , SipSubscribe.Event okEvent) throws SipException, InvalidArgumentException, ParseException;


    /**
     * 向上级回复通道信息
     * @param channel 通道信息
     * @param parentPlatform 平台信息
     * @param sn
     * @param fromTag
     * @param size
     * @return
     */
    void catalogQuery(DeviceChannel channel, ParentPlatform parentPlatform, String sn, String fromTag, int size) throws SipException, InvalidArgumentException, ParseException;

    /**
     * 向上级回复DeviceInfo查询信息
     * @param parentPlatform 平台信息
     * @param sn SN
     * @param fromTag FROM头的tag信息
     * @return
     */
    void deviceInfoResponse(ParentPlatform parentPlatform, String sn, String fromTag) throws SipException, InvalidArgumentException, ParseException;

    /**
     * 向上级回复DeviceStatus查询信息
     * @param parentPlatform 平台信息
     * @param sn
     * @param fromTag
     * @return
     */
    void deviceStatusResponse(ParentPlatform parentPlatform,String channelId, String sn, String fromTag,boolean status) throws SipException, InvalidArgumentException, ParseException;



    /**
     * 回复recordInfo
     * @param parentPlatform 平台信息
     * @param fromTag fromTag
     * @param recordInfo 录像信息
     */
    void recordInfo(ParentPlatform parentPlatform, String fromTag, RecordInfo recordInfo) throws SipException, InvalidArgumentException, ParseException;

    /**
     * 录像播放推送完成时发送MediaStatus消息
     * @param platform
     * @param sendRtpItem
     * @return
     */
    void sendMediaStatusNotify(ParentPlatform platform, PlatformInviteRtpItem sendRtpItem) throws SipException, InvalidArgumentException, ParseException;

    /**
     * 向发起点播的上级回复bye
     * @param platform 平台信息
     * @param callId  callId
     */
    void streamByeCmd(ParentPlatform platform, String callId) throws SipException, InvalidArgumentException, ParseException;
    void streamByeCmd(ParentPlatform platform, PlatformInviteRtpItem sendRtpItem) throws SipException, InvalidArgumentException, ParseException;

    void inviteResponse(SIPRequest request, PlatformInviteRtpItem sendRtpItem) throws SipException, InvalidArgumentException, ParseException;


}
