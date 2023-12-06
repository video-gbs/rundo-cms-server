package com.runjian.cascade.gb28181.transmit.event.request.impl.message.query.cmd;

import com.runjian.cascade.gb28181.bean.Device;
import com.runjian.cascade.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.cascade.gb28181.transmit.event.request.impl.message.IMessageHandler;
import com.runjian.cascade.gb28181.transmit.event.request.impl.message.query.QueryMessageHandler;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.message.Response;
import java.text.ParseException;

@Component
@Slf4j
public class AlarmQueryMessageHandler extends SIPRequestProcessorParent implements InitializingBean, IMessageHandler {

    private Logger logger = LoggerFactory.getLogger(AlarmQueryMessageHandler.class);
    private final String cmdType = "Alarm";

    @Autowired
    private QueryMessageHandler queryMessageHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        queryMessageHandler.addHandler(cmdType, this);
    }


    @Override
    public void handForPlatform(RequestEvent evt, ParentPlatform parentPlatform, Element rootElement) {
        log.info(LogTemplate.PROCESS_LOG_TEMPLATE,"级联sip","告警查询",evt.getRequest());
        try {
             responseAck((SIPRequest) evt.getRequest(), Response.NOT_FOUND, "not support alarm query");
        } catch (SipException | InvalidArgumentException | ParseException e) {
            logger.error("[命令发送失败] 国标级联 alarm查询回复200OK: {}", e.getMessage());
        }

    }
}
