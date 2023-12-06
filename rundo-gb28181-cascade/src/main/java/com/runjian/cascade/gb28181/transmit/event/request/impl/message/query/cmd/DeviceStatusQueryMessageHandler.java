package com.runjian.cascade.gb28181.transmit.event.request.impl.message.query.cmd;

import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181.transmit.cmd.impl.SIPCommanderFroPlatform;
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
import javax.sip.header.FromHeader;
import javax.sip.message.Response;
import java.text.ParseException;

import static com.runjian.cascade.gb28181.utils.XmlUtil.getText;


@Component
@Slf4j
public class DeviceStatusQueryMessageHandler extends SIPRequestProcessorParent implements InitializingBean, IMessageHandler {

    private final String cmdType = "DeviceStatus";

    @Autowired
    private QueryMessageHandler queryMessageHandler;


    @Autowired
    private SIPCommanderFroPlatform cmderFroPlatform;

    @Autowired
    private SipConfig config;

    @Override
    public void afterPropertiesSet() throws Exception {
        queryMessageHandler.addHandler(cmdType, this);
    }


    @Override
    public void handForPlatform(RequestEvent evt, ParentPlatform parentPlatform, Element rootElement) {

        log.info(LogTemplate.PROCESS_LOG_TEMPLATE,"级联sip","状态查询",evt.getRequest());
        FromHeader fromHeader = (FromHeader) evt.getRequest().getHeader(FromHeader.NAME);
        // 回复200 OK
        try {
            responseAck((SIPRequest) evt.getRequest(), Response.OK);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] 国标级联 DeviceStatus查询回复200OK: {}", e.getMessage());
        }
        String sn = rootElement.element("SN").getText();
        String channelId = getText(rootElement, "DeviceID");

        try {
            cmderFroPlatform.deviceStatusResponse(parentPlatform,channelId, sn, fromHeader.getTag(),true);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] 国标级联 DeviceStatus查询回复: {}", e.getMessage());
        }
    }
}
