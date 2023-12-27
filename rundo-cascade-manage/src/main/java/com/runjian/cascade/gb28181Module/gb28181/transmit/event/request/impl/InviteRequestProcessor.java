package com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl;

import com.runjian.cascade.gb28181Module.gb28181.bean.Gb28181Sdp;
import com.runjian.cascade.gb28181Module.gb28181.bean.PlatformInviteRtpItem;
import com.runjian.cascade.gb28181Module.gb28181.transmit.SIPProcessorObserver;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.ISIPRequestProcessor;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.cascade.gb28181Module.gb28181.utils.SipUtils;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sdp.TimeDescriptionImpl;
import gov.nist.javax.sdp.fields.TimeField;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.sdp.Media;
import javax.sdp.MediaDescription;
import javax.sdp.SessionDescription;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Response;
import java.text.ParseException;
import java.time.Instant;
import java.util.Vector;
import com.runjian.cascade.gb28181Module.gb28181.transmit.ISIPProcessorObserver;

/**
 * SIP命令类型： INVITE请求
 */
@SuppressWarnings("rawtypes")
@Component
@Slf4j
public class InviteRequestProcessor extends SIPRequestProcessorParent implements InitializingBean, ISIPRequestProcessor {


    private final String method = "INVITE";

    @Autowired
    private SIPProcessorObserver sipProcessorObserver;


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ISIPCommanderForPlatform sipCommanderForPlatform;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加消息处理的订阅
        sipProcessorObserver.addRequestProcessor(method, this);
    }

    /**
     * 处理invite请求
     *
     * @param evt 请求消息
     */
    @Override
    public void process(RequestEvent evt) {

        try {
            SIPRequest request = (SIPRequest) evt.getRequest();
            log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "SIP命令INVITE请求处理", "收到请求信息", request.toString());

            String platformId = SipUtils.getUserIdFromFromHeader(request);
            String channelId = SipUtils.getChannelIdFromRequest(request);
            CallIdHeader callIdHeader = (CallIdHeader) request.getHeader(CallIdHeader.NAME);
            String callId = callIdHeader.getCallId();
            if (platformId == null) {
                log.info(LogTemplate.PROCESS_LOG_TEMPLATE, "SIP命令INVITE请求处理", "无法从FromHeader的Address中获取到平台id，返回400");
                // 参数不全， 发400，请求错误
                try {
                    responseAck(request, Response.BAD_REQUEST);
                } catch (SipException | InvalidArgumentException | ParseException e) {
                    log.error(LogTemplate.ERROR_LOG_TEMPLATE, "SIP命令INVITE请求处理", "命令发送失败，invite BAD_REQUEST", e);
                }
                return;
            }
            // todo  1.判断平台是否注册和在线，2.获取点播的ssrc,流媒体传输ip,流媒体传输端口，传输协议，点播方式：play/playback等等

            // 解析sdp消息, 使用jainsip 自带的sdp解析方式
            String contentString = new String(request.getRawContent());

            Gb28181Sdp gb28181Sdp = SipUtils.parseSDP(contentString);
            SessionDescription sdp = gb28181Sdp.getBaseSdb();
            String sessionName = sdp.getSessionName().getValue();

            Long startTime = null;
            Long stopTime = null;
            Instant start = null;
            Instant end = null;
            if (sdp.getTimeDescriptions(false) != null && !sdp.getTimeDescriptions(false).isEmpty()) {
                TimeDescriptionImpl timeDescription = (TimeDescriptionImpl) (sdp.getTimeDescriptions(false).get(0));
                TimeField startTimeFiled = (TimeField) timeDescription.getTime();
                startTime = startTimeFiled.getStartTime();
                stopTime = startTimeFiled.getStopTime();

                start = Instant.ofEpochSecond(startTime);
                end = Instant.ofEpochSecond(stopTime);
            }
            //  获取支持的格式
            Vector mediaDescriptions = sdp.getMediaDescriptions(true);
            // 查看是否支持PS 负载96
            //String ip = null;
            int port = -1;
            String protocol = "RTP/AVP";
            int streamProtocal = 0;
            for (Object description : mediaDescriptions) {
                MediaDescription mediaDescription = (MediaDescription) description;
                Media media = mediaDescription.getMedia();

                Vector mediaFormats = media.getMediaFormats(false);
                if (mediaFormats.contains("96")) {
                    port = media.getMediaPort();
                    //String mediaType = media.getMediaType();
                    protocol = media.getProtocol();

                    // 区分TCP发流还是udp， 当前默认udp
                    if ("TCP/RTP/AVP".equalsIgnoreCase(protocol)) {
                        String setup = mediaDescription.getAttribute("setup");
                        if (setup != null) {
                            if ("active".equalsIgnoreCase(setup)) {
                                streamProtocal = 2;
                            } else if ("passive".equalsIgnoreCase(setup)) {
                                streamProtocal = 1;
                            }
                        }
                    }
                    break;
                }
            }
            if (port == -1) {
                log.info("不支持的媒体格式，返回415");
                // 回复不支持的格式
                try {
                    // 不支持的格式，发415
                    responseAck(request, Response.UNSUPPORTED_MEDIA_TYPE);
                } catch (SipException | InvalidArgumentException | ParseException e) {
                    log.error("[命令发送失败] invite 不支持的格式: {}", e.getMessage());
                }
                return;
            }
            String ssrc = gb28181Sdp.getSsrc();
            PlatformInviteRtpItem platformInviteRtpItem = new PlatformInviteRtpItem();
            platformInviteRtpItem.setPlatformId(platformId);
            platformInviteRtpItem.setChannelId(channelId);
            platformInviteRtpItem.setStreamProtocal(streamProtocal);
            platformInviteRtpItem.setSsrc(ssrc);
            platformInviteRtpItem.setCallId(callId);
            platformInviteRtpItem.setFromTag(request.getFromTag());
            platformInviteRtpItem.setToTag(request.getToTag());
            platformInviteRtpItem.setToTag(request.getToTag());
// todo           platformInviteRtpItem.setLocalPort("本平台端口");
            //获取本级联平台的推流端口
            platformInviteRtpItem.setSessionName(sessionName);
            platformInviteRtpItem.setStartTime(startTime);
            platformInviteRtpItem.setStopTime(stopTime);




            //todo feign  通知本平台的设备通道的点播 ---点播失败直接返回不进行后续流程，


            //todo 设备点播成果 进行invite指令的h回复
            sipCommanderForPlatform.inviteResponse(request,platformInviteRtpItem);


        } catch (Exception e) {
            log.error(LogTemplate.ERROR_LOG_TEMPLATE,"级联点播","处理失败", e.getMessage());
        }
    }
}
