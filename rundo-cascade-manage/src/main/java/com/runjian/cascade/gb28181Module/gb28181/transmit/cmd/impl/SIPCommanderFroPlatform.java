package com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.impl;

import com.runjian.cascade.gb28181Module.gb28181.SipLayer;
import com.runjian.cascade.gb28181Module.gb28181.bean.*;
import com.runjian.cascade.gb28181Module.gb28181.event.SipSubscribe;
import com.runjian.cascade.gb28181Module.gb28181.transmit.SIPSender;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.SIPRequestHeaderPlarformProvider;
import com.runjian.cascade.gb28181Module.gb28181.utils.DateUtil;
import com.runjian.cascade.gb28181Module.gb28181.utils.SipUtils;
import com.runjian.cascade.gb28181Module.service.IRedisCatchStorageService;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@DependsOn("sipLayer")
public class SIPCommanderFroPlatform implements ISIPCommanderForPlatform {

    private final Logger logger = LoggerFactory.getLogger(SIPCommanderFroPlatform.class);

    @Autowired
    private SIPRequestHeaderPlarformProvider headerProviderPlatformProvider;

    @Autowired
    private IRedisCatchStorageService redisCatchStorage;


    @Autowired
    private SipLayer sipLayer;

    @Autowired
    private SIPSender sipSender;

    @Autowired
    private SipSubscribe sipSubscribe;
    @Override
    public void register(ParentPlatform parentPlatform, SipSubscribe.Event errorEvent, SipSubscribe.Event okEvent) throws InvalidArgumentException, ParseException, SipException {
        register(parentPlatform, null, null, errorEvent, okEvent, true);
    }

    @Override
    public void register(ParentPlatform parentPlatform, SipTransactionInfo sipTransactionInfo, SipSubscribe.Event errorEvent, SipSubscribe.Event okEvent) throws InvalidArgumentException, ParseException, SipException {
        register(parentPlatform, sipTransactionInfo, null, errorEvent, okEvent, true);
    }

    @Override
    public void register(ParentPlatform parentPlatform, SipTransactionInfo sipTransactionInfo, WWWAuthenticateHeader www, SipSubscribe.Event errorEvent, SipSubscribe.Event okEvent, boolean isRegister) throws SipException, InvalidArgumentException, ParseException {
        Request request;
        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(parentPlatform.getTransport());
        String fromTag = SipUtils.getNewFromTag();
        String toTag = null;
        if (sipTransactionInfo != null ) {
            if (sipTransactionInfo.getCallId() != null) {
                callIdHeader.setCallId(sipTransactionInfo.getCallId());
            }
            if (sipTransactionInfo.getFromTag() != null) {
                fromTag = sipTransactionInfo.getFromTag();
            }
            if (sipTransactionInfo.getToTag() != null) {
                toTag = sipTransactionInfo.getToTag();
            }
        }

        if (www == null ) {
            request = headerProviderPlatformProvider.createRegisterRequest(parentPlatform,
                    redisCatchStorage.getCSEQ(), fromTag,
                    toTag, callIdHeader, isRegister? parentPlatform.getExpires() : 0);
            // 将 callid 写入缓存， 等注册成功可以更新状态
            // 将 callid 写入缓存， 等注册成功可以更新状态
            String callIdFromHeader = callIdHeader.getCallId();
            redisCatchStorage.updatePlatformRegisterInfo(callIdFromHeader, PlatformRegisterInfo.getInstance(parentPlatform.getServerGbId(), isRegister));

            sipSubscribe.addErrorSubscribe(callIdHeader.getCallId(), (event)->{
                if (event != null) {
                    logger.info("向上级平台 [ {} ] 注册发生错误： {} ",
                            parentPlatform.getServerGbId(),
                            event.msg);
                }
                redisCatchStorage.delPlatformRegisterInfo(callIdFromHeader);
                if (errorEvent != null ) {
                    errorEvent.response(event);
                }
            });


        }else {
            request = headerProviderPlatformProvider.createRegisterRequest(parentPlatform, fromTag, toTag, www, callIdHeader, isRegister? parentPlatform.getExpires() : 0);
        }

        sipSender.transmitRequest(request, null, okEvent);
    }

    @Override
    public void unregister(ParentPlatform parentPlatform, SipTransactionInfo sipTransactionInfo, SipSubscribe.Event errorEvent, SipSubscribe.Event okEvent) throws InvalidArgumentException, ParseException, SipException {
        register(parentPlatform, sipTransactionInfo, null, errorEvent, okEvent, false);
    }

    @Override
    public String keepalive(ParentPlatform parentPlatform, SipSubscribe.Event errorEvent, SipSubscribe.Event okEvent) throws SipException, InvalidArgumentException, ParseException {
        String characterSet = parentPlatform.getCharacterSet();
        StringBuffer keepaliveXml = new StringBuffer(200);
        keepaliveXml.append("<?xml version=\"1.0\" encoding=\"")
                .append(characterSet).append("\"?>\r\n")
                .append("<Notify>\r\n")
                .append("<CmdType>Keepalive</CmdType>\r\n")
                .append("<SN>" + (int)((Math.random()*9+1)*100000) + "</SN>\r\n")
                .append("<DeviceID>" + parentPlatform.getDeviceGbId() + "</DeviceID>\r\n")
                .append("<Status>OK</Status>\r\n")
                .append("</Notify>\r\n");

        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(parentPlatform.getTransport());

        Request request = headerProviderPlatformProvider.createMessageRequest(
                parentPlatform,
                keepaliveXml.toString(),
                SipUtils.getNewFromTag(),
                SipUtils.getNewViaTag(),
                callIdHeader);
        sipSender.transmitRequest(request, errorEvent, okEvent);
        return callIdHeader.getCallId();
    }

    @Override
    public void catalogQuery(DeviceChannel channel, ParentPlatform parentPlatform, String sn, String fromTag, int size) throws SipException, InvalidArgumentException, ParseException {

        if ( parentPlatform ==null) {
            return ;
        }
        List<DeviceChannel> channels = new ArrayList<>();
        if (channel != null) {
            channels.add(channel);
        }
        String catalogXml = getCatalogXml(channels, sn, parentPlatform, size);

        // callid
        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(parentPlatform.getTransport());

        Request request = headerProviderPlatformProvider.createMessageRequest(parentPlatform, catalogXml.toString(), fromTag, SipUtils.getNewViaTag(), callIdHeader);
        sipSender.transmitRequest(request);
    }

    private String getCatalogXml(List<DeviceChannel> channels, String sn, ParentPlatform parentPlatform, int size) {
        String characterSet = parentPlatform.getCharacterSet();
        StringBuffer catalogXml = new StringBuffer(600);
        catalogXml.append("<?xml version=\"1.0\" encoding=\"" + characterSet +"\"?>\r\n")
                .append("<Response>\r\n")
                .append("<CmdType>Catalog</CmdType>\r\n")
                .append("<SN>" +sn + "</SN>\r\n")
                .append("<DeviceID>" + parentPlatform.getDeviceGbId() + "</DeviceID>\r\n")
                .append("<SumNum>" + size + "</SumNum>\r\n")
                .append("<DeviceList Num=\"" + channels.size() +"\">\r\n");
        if (channels.size() > 0) {
            for (DeviceChannel channel : channels) {
                if (parentPlatform.getServerGbId().equals(channel.getParentId())) {
                    channel.setParentId(parentPlatform.getDeviceGbId());
                }
                catalogXml.append("<Item>\r\n");
                // 行政区划分组只需要这两项就可以
                catalogXml.append("<DeviceID>" + channel.getChannelId() + "</DeviceID>\r\n");
                catalogXml.append("<Name>" + channel.getChannelName() + "</Name>\r\n");
                if (channel.getChannelId().length() <= 8) {
                    catalogXml.append("</Item>\r\n");
                    continue;
                }else {
                    if (channel.getChannelId().length() != 20) {
                        catalogXml.append("</Item>\r\n");
                        logger.warn("[编号长度异常] {} 长度错误，请使用20位长度的国标编号,当前长度：{}", channel.getChannelId(), channel.getChannelId().length());
                        catalogXml.append("</Item>\r\n");
                        continue;
                    }
                    switch (Integer.parseInt(channel.getChannelId().substring(10, 13))){
                        case 200:
//                            catalogXml.append("<Manufacturer>三永华通</Manufacturer>\r\n");
//                            GitUtil gitUtil = SpringBeanFactory.getBean("gitUtil");
//                            String model = (gitUtil == null || gitUtil.getBuildVersion() == null)?"1.0": gitUtil.getBuildVersion();
//                            catalogXml.append("<Model>" + model + "</Manufacturer>\r\n");
//                            catalogXml.append("<Owner>三永华通</Owner>\r\n");
                            if (channel.getCivilCode() != null) {
                                catalogXml.append("<CivilCode>"+channel.getCivilCode()+"</CivilCode>\r\n");
                            }else {
                                catalogXml.append("<CivilCode></CivilCode>\r\n");
                            }

                            catalogXml.append("<RegisterWay>1</RegisterWay>\r\n");
                            catalogXml.append("<Secrecy>0</Secrecy>\r\n");
                            break;
                        case 215:
                            if (!ObjectUtils.isEmpty(channel.getParentId())) {
                                catalogXml.append("<ParentID>" + channel.getParentId() + "</ParentID>\r\n");
                            }

                            break;
                        case 216:
                            if (!ObjectUtils.isEmpty(channel.getParentId())) {
                                catalogXml.append("<ParentID>" + channel.getParentId() + "</ParentID>\r\n");
                            }else {
                                catalogXml.append("<ParentID></ParentID>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getBusinessGroupId())) {
                                catalogXml.append("<BusinessGroupID>" + channel.getBusinessGroupId() + "</BusinessGroupID>\r\n");
                            }else {
                                catalogXml.append("<BusinessGroupID></BusinessGroupID>\r\n");
                            }
                            break;
                        default:
                            // 通道项
                            if (channel.getManufacturer() != null) {
                                catalogXml.append("<Manufacturer>" + channel.getManufacturer() + "</Manufacturer>\r\n");
                            }else {
                                catalogXml.append("<Manufacturer></Manufacturer>\r\n");
                            }
                            if (channel.getSecrecy() != null) {
                                catalogXml.append("<Secrecy>" + channel.getSecrecy() + "</Secrecy>\r\n");
                            }else {
                                catalogXml.append("<Secrecy></Secrecy>\r\n");
                            }
                            catalogXml.append("<RegisterWay>" + channel.getRegisterWay() + "</RegisterWay>\r\n");
                            if (channel.getModel() != null) {
                                catalogXml.append("<Model>" + channel.getModel() + "</Model>\r\n");
                            }else {
                                catalogXml.append("<Model></Model>\r\n");
                            }
                            if (channel.getOwner() != null) {
                                catalogXml.append("<Owner>" + channel.getOwner()+ "</Owner>\r\n");
                            }else {
                                catalogXml.append("<Owner></Owner>\r\n");
                            }
                            if (channel.getCivilCode() != null) {
                                catalogXml.append("<CivilCode>" + channel.getCivilCode() + "</CivilCode>\r\n");
                            }else {
                                catalogXml.append("<CivilCode></CivilCode>\r\n");
                            }
                            if (channel.getAddress() == null) {
                                catalogXml.append("<Address></Address>\r\n");
                            }else {
                                catalogXml.append("<Address>" + channel.getAddress() + "</Address>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getParentId())) {
                                catalogXml.append("<ParentID>" + channel.getParentId() + "</ParentID>\r\n");
                            }else {
                                catalogXml.append("<ParentID></ParentID>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getBlock())) {
                                catalogXml.append("<Block>" + channel.getBlock() + "</Block>\r\n");
                            }else {
                                catalogXml.append("<Block></Block>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getSafetyWay())) {
                                catalogXml.append("<SafetyWay>" + channel.getSafetyWay() + "</SafetyWay>\r\n");
                            }else {
                                catalogXml.append("<SafetyWay></SafetyWay>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getCertNum())) {
                                catalogXml.append("<CertNum>" + channel.getCertNum() + "</CertNum>\r\n");
                            }else {
                                catalogXml.append("<CertNum></CertNum>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getCertifiable())) {
                                catalogXml.append("<Certifiable>" + channel.getCertifiable() + "</Certifiable>\r\n");
                            }else {
                                catalogXml.append("<Certifiable></Certifiable>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getErrCode())) {
                                catalogXml.append("<ErrCode>" + channel.getErrCode() + "</ErrCode>\r\n");
                            }else {
                                catalogXml.append("<ErrCode></ErrCode>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getEndTime())) {
                                catalogXml.append("<EndTime>" + channel.getEndTime() + "</EndTime>\r\n");
                            }else {
                                catalogXml.append("<EndTime></EndTime>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getSecrecy())) {
                                catalogXml.append("<Secrecy>" + channel.getSecrecy() + "</Secrecy>\r\n");
                            }else {
                                catalogXml.append("<Secrecy></Secrecy>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getIpAddress())) {
                                catalogXml.append("<IPAddress>" + channel.getIpAddress() + "</IPAddress>\r\n");
                            }else {
                                catalogXml.append("<IPAddress></IPAddress>\r\n");
                            }
                            catalogXml.append("<Port>" + channel.getPort() + "</Port>\r\n");
                            if (!ObjectUtils.isEmpty(channel.getPassword())) {
                                catalogXml.append("<Password>" + channel.getPassword() + "</Password>\r\n");
                            }else {
                                catalogXml.append("<Password></Password>\r\n");
                            }
                            if (!ObjectUtils.isEmpty(channel.getPtzType())) {
                                catalogXml.append("<PTZType>" + channel.getPtzType() + "</PTZType>\r\n");
                            }else {
                                catalogXml.append("<PTZType></PTZType>\r\n");
                            }
                            catalogXml.append("<Status>" + (channel.getStatus() == 1 ?"ON":"OFF") + "</Status>\r\n");

                            break;

                    }
                    catalogXml.append("</Item>\r\n");
                }
            }
        }

        catalogXml.append("</DeviceList>\r\n");
        catalogXml.append("</Response>\r\n");
        return catalogXml.toString();
    }



    @Override
    public void deviceInfoResponse(ParentPlatform parentPlatform,  String sn, String fromTag) throws SipException, InvalidArgumentException, ParseException {
        if (parentPlatform == null) {
            return;
        }
        String deviceId = parentPlatform.getServerGbId() ;
        String deviceName =  parentPlatform.getName() ;
        String manufacturer = "rundo-edge";
        String model = "platform";
        String firmware =  "1.1.0";
        String characterSet = parentPlatform.getCharacterSet();
        StringBuffer deviceInfoXml = new StringBuffer(600);
        deviceInfoXml.append("<?xml version=\"1.0\" encoding=\"" + characterSet + "\"?>\r\n");
        deviceInfoXml.append("<Response>\r\n");
        deviceInfoXml.append("<CmdType>DeviceInfo</CmdType>\r\n");
        deviceInfoXml.append("<SN>" +sn + "</SN>\r\n");
        deviceInfoXml.append("<DeviceID>" + deviceId + "</DeviceID>\r\n");
        deviceInfoXml.append("<DeviceName>" + deviceName + "</DeviceName>\r\n");
        deviceInfoXml.append("<Manufacturer>" + manufacturer + "</Manufacturer>\r\n");
        deviceInfoXml.append("<Model>" + model + "</Model>\r\n");
        deviceInfoXml.append("<Firmware>" + firmware + "</Firmware>\r\n");
        deviceInfoXml.append("<Result>OK</Result>\r\n");
        deviceInfoXml.append("</Response>\r\n");

        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(parentPlatform.getTransport());

        Request request = headerProviderPlatformProvider.createMessageRequest(parentPlatform, deviceInfoXml.toString(), fromTag, SipUtils.getNewViaTag(), callIdHeader);
        sipSender.transmitRequest(request);
    }

    @Override
    public void deviceStatusResponse(ParentPlatform parentPlatform, String channelId, String sn, String fromTag, boolean status) throws SipException, InvalidArgumentException, ParseException {
        if (parentPlatform == null) {
            return ;
        }
        String statusStr = (status)?"ONLINE":"OFFLINE";
        String characterSet = parentPlatform.getCharacterSet();
        StringBuffer deviceStatusXml = new StringBuffer(600);
        deviceStatusXml.append("<?xml version=\"1.0\" encoding=\"" + characterSet + "\"?>\r\n")
                .append("<Response>\r\n")
                .append("<CmdType>DeviceStatus</CmdType>\r\n")
                .append("<SN>" +sn + "</SN>\r\n")
                .append("<DeviceID>" + channelId + "</DeviceID>\r\n")
                .append("<Result>OK</Result>\r\n")
                .append("<Online>"+statusStr+"</Online>\r\n")
                .append("<Status>OK</Status>\r\n")
                .append("</Response>\r\n");

        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(parentPlatform.getTransport());

        Request request = headerProviderPlatformProvider.createMessageRequest(parentPlatform, deviceStatusXml.toString(), fromTag, SipUtils.getNewViaTag(), callIdHeader);
        sipSender.transmitRequest(request);
    }

    @Override
    public void recordInfo(ParentPlatform parentPlatform, String fromTag, RecordInfo recordInfo) throws SipException, InvalidArgumentException, ParseException {
        if ( parentPlatform ==null) {
            return ;
        }
        String characterSet = parentPlatform.getCharacterSet();
        StringBuffer recordXml = new StringBuffer(600);
        recordXml.append("<?xml version=\"1.0\" encoding=\"" + characterSet + "\"?>\r\n")
                .append("<Response>\r\n")
                .append("<CmdType>RecordInfo</CmdType>\r\n")
                .append("<SN>" +recordInfo.getSn() + "</SN>\r\n")
                .append("<Name>" +recordInfo.getName() + "</Name>\r\n")
                .append("<DeviceID>" + recordInfo.getChannelId() + "</DeviceID>\r\n")
                .append("<SumNum>" + recordInfo.getSumNum() + "</SumNum>\r\n");
        if (recordInfo.getRecordList() == null ) {
            recordXml.append("<RecordList Num=\"0\">\r\n");
        }else {
            recordXml.append("<RecordList Num=\"" + recordInfo.getRecordList().size()+"\">\r\n");
            if (recordInfo.getRecordList().size() > 0) {
                for (RecordItem recordItem : recordInfo.getRecordList()) {
                    recordXml.append("<Item>\r\n");
                    recordXml.append("<DeviceID>" + recordItem.getDeviceId() + "</DeviceID>\r\n")
                            .append("<Name>" + recordItem.getName() + "</Name>\r\n")
                            .append("<StartTime>" + DateUtil.yyyy_MM_dd_HH_mm_ssToISO8601(recordItem.getStartTime()) + "</StartTime>\r\n")
                            .append("<EndTime>" + DateUtil.yyyy_MM_dd_HH_mm_ssToISO8601(recordItem.getEndTime()) + "</EndTime>\r\n")
                            .append("<Secrecy>" + recordItem.getSecrecy() + "</Secrecy>\r\n")
                            .append("<Type>" + recordItem.getType() + "</Type>\r\n");
                    if (!ObjectUtils.isEmpty(recordItem.getFileSize())) {
                        recordXml.append("<FileSize>" + recordItem.getFileSize() + "</FileSize>\r\n");
                    }
                    if (!ObjectUtils.isEmpty(recordItem.getFilePath())) {
                        recordXml.append("<FilePath>" + recordItem.getFilePath() + "</FilePath>\r\n");
                    }
                    recordXml.append("</Item>\r\n");
                }
            }
        }

        recordXml.append("</RecordList>\r\n")
                .append("</Response>\r\n");

        // callid
        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(parentPlatform.getTransport());

        Request request = headerProviderPlatformProvider.createMessageRequest(parentPlatform, recordXml.toString(), fromTag, SipUtils.getNewViaTag(), callIdHeader);
        sipSender.transmitRequest(request);
    }

    @Override
    public void sendMediaStatusNotify(ParentPlatform platform, PlatformInviteRtpItem sendRtpItem) throws SipException, InvalidArgumentException, ParseException {
        if (sendRtpItem == null || platform == null) {
            return;
        }


        String characterSet = platform.getCharacterSet();
        StringBuffer mediaStatusXml = new StringBuffer(200);
        mediaStatusXml.append("<?xml version=\"1.0\" encoding=\"" + characterSet + "\"?>\r\n")
                .append("<Notify>\r\n")
                .append("<CmdType>MediaStatus</CmdType>\r\n")
                .append("<SN>" + (int)((Math.random()*9+1)*100000) + "</SN>\r\n")
                .append("<DeviceID>" + sendRtpItem.getChannelId() + "</DeviceID>\r\n")
                .append("<NotifyType>121</NotifyType>\r\n")
                .append("</Notify>\r\n");

        SIPRequest messageRequest = (SIPRequest)headerProviderPlatformProvider.createMessageRequest(platform, mediaStatusXml.toString(),
                sendRtpItem);

        sipSender.transmitRequest(messageRequest);
    }

    @Override
    public void streamByeCmd(ParentPlatform platform, String callId) throws SipException, InvalidArgumentException, ParseException {
        if (platform == null) {
            return;
        }
        //查询点播时候的 sip信息
//        SendRtpItem sendRtpItem = redisCatchStorage.querySendRTPServer(platform.getServerGBId(), null, null, callId);
//        if (sendRtpItem != null) {
//            streamByeCmd(platform, sendRtpItem);
//        }
    }

    @Override
    public void streamByeCmd(ParentPlatform parentPlatform, PlatformInviteRtpItem sendRtpItem) throws SipException, InvalidArgumentException, ParseException {
        if (sendRtpItem == null ) {
            logger.info("[向上级发送BYE]， sendRtpItem 为NULL");
            return;
        }
        if (parentPlatform == null) {
            logger.info("[向上级发送BYE]， platform 为NULL");
            return;
        }
        logger.info("[向上级发送BYE]， {}/{}", parentPlatform.getServerGbId(), sendRtpItem.getChannelId());

        SIPRequest byeRequest = headerProviderPlatformProvider.createByeRequest(parentPlatform, sendRtpItem);
        if (byeRequest == null) {
            logger.warn("[向上级发送bye]：无法创建 byeRequest");
        }
        sipSender.transmitRequest(byeRequest);
    }

    @Override
    public void inviteResponse(SIPRequest request,  PlatformInviteRtpItem sendRtpItem) throws SipException, InvalidArgumentException, ParseException {
        StringBuffer content = new StringBuffer(200);
        content.append("v=0\r\n");
        content.append("o=" + sendRtpItem.getChannelId() + " 0 0 IN IP4 " + sendRtpItem.getPlatformSdp() + "\r\n");
        content.append("s=" + sendRtpItem.getSessionName() + "\r\n");
        content.append("c=IN IP4 " + sendRtpItem.getPlatformSdp() + "\r\n");
        if (sendRtpItem.getSessionName().equalsIgnoreCase(InviteStreamType.Play.name())) {
            content.append("t=0 0\r\n");

        } else {
            content.append("t=" + sendRtpItem.getStartTime() + " " + sendRtpItem.getStopTime() + "\r\n");
        }
        int localPort = sendRtpItem.getLocalPort();
        if (localPort == 0) {
            // 非严格模式端口不统一, 增加兼容性，修改为一个不为0的端口
            localPort = new Random().nextInt(65535) + 1;
        }
        if(sendRtpItem.getStreamProtocal() == 0){
            content.append("m=video " + localPort + " RTP/AVP 96\r\n");
        }else {
            content.append("m=video " + localPort + " TCP/RTP/AVP 96\r\n");
        }
        content.append("a=sendonly\r\n");
        content.append("a=rtpmap:96 PS/90000\r\n");
        content.append("y=" + sendRtpItem.getSsrc() + "\r\n");
        content.append("f=\r\n");

        HeaderFactory headerFactory = SipFactory.getInstance().createHeaderFactory();
        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("APPLICATION", "SDP");
        // 兼容国标中的使用编码@域名作为RequestURI的情况
        SipURI sipUri = (SipURI)request.getRequestURI();
        if (request.getToHeader().getTag() == null) {
            request.getToHeader().setTag(SipUtils.getNewTag());
        }
        SIPResponse response = (SIPResponse)SipFactory.getInstance().createMessageFactory().createResponse(Response.OK, request);


        Address concatAddress = SipFactory.getInstance().createAddressFactory().createAddress(
                SipFactory.getInstance().createAddressFactory().createSipURI(sipUri.getUser(),  sipUri.getHost()+":"+sipUri.getPort()
                ));
        response.addHeader(SipFactory.getInstance().createHeaderFactory().createContactHeader(concatAddress));
        response.setContent(content, contentTypeHeader);
        // 发送response
        sipSender.transmitRequest(response);
    }

}
