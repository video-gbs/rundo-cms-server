package com.runjian.cascade.gb28181.transmit.event.request.impl.message;

import com.runjian.cascade.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181.event.SipSubscribe;
import com.runjian.cascade.gb28181.transmit.SIPProcessorObserver;
import com.runjian.cascade.gb28181.transmit.event.request.ISIPRequestProcessor;
import com.runjian.cascade.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.cascade.gb28181.utils.SipUtils;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MessageRequestProcessor extends SIPRequestProcessorParent implements InitializingBean, ISIPRequestProcessor {


    private final String method = "MESSAGE";

    private static Map<String, IMessageHandler> messageHandlerMap = new ConcurrentHashMap<>();

    @Autowired
    private SIPProcessorObserver sipProcessorObserver;


    @Autowired
    private SipSubscribe sipSubscribe;



    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加消息处理的订阅
        sipProcessorObserver.addRequestProcessor(method, this);
    }

    public void addHandler(String name, IMessageHandler handler) {
        messageHandlerMap.put(name, handler);
    }

    @Override
    public void process(RequestEvent evt) {
        SIPRequest sipRequest = (SIPRequest)evt.getRequest();
//        logger.info("接收到消息：" + evt.getRequest());
        String deviceId = SipUtils.getUserIdFromFromHeader(evt.getRequest());
        CallIdHeader callIdHeader = sipRequest.getCallIdHeader();
        SIPRequest request = (SIPRequest) evt.getRequest();

        //todo  查询上级平台是否存在
        ParentPlatform parentPlatform = new ParentPlatform();
        try {

            Element rootElement = null;
            try {
                rootElement = getRootElement(evt);
            } catch (DocumentException e) {
                log.warn(LogTemplate.ERROR_LOG_TEMPLATE,"级联sip--message解析失败", e);
                // 不存在则回复404
                responseAck(request, Response.BAD_REQUEST, e.getMessage());
            }
            String name = rootElement.getName();
            IMessageHandler messageHandler = messageHandlerMap.get(name);
            if (messageHandler != null) {

                messageHandler.handForPlatform(evt, parentPlatform, rootElement);
            }else {
                // 不支持的message
                // 不存在则回复415
                responseAck(request, Response.UNSUPPORTED_MEDIA_TYPE, "Unsupported message type, must Control/Notify/Query/Response");
            }
        } catch (Exception e) {
            log.warn(LogTemplate.ERROR_LOG_TEMPLATE,"级联sip--message异常失败", e);
        }

    }


}
