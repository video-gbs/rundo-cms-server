package com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.query.cmd;

import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.impl.SIPCommanderFroPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.IMessageHandler;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.query.QueryMessageHandler;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.header.FromHeader;
import javax.sip.message.Response;
import java.text.ParseException;

@Component
@Slf4j
public class CatalogQueryMessageHandler extends SIPRequestProcessorParent implements InitializingBean, IMessageHandler {

    private final String cmdType = "Catalog";

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
        log.info(LogTemplate.PROCESS_LOG_TEMPLATE,"级联sip","目录查询",evt.getRequest());
        FromHeader fromHeader = (FromHeader) evt.getRequest().getHeader(FromHeader.NAME);
        try {
            // 回复200 OK
             responseAck((SIPRequest) evt.getRequest(), Response.OK);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] 国标级联 目录查询回复200OK: {}", e.getMessage());
        }
        Element snElement = rootElement.element("SN");
        String sn = snElement.getText();

        //查询全部的

        try {
            //查找本级的目录  以及通道


            cmderFroPlatform.catalogQuery(null, parentPlatform, sn, fromHeader.getTag(), 0);

        }catch (Exception e){
            log.error(LogTemplate.ERROR_LOG_TEMPLATE,"级联sip--处理目录消息的通知", e.getMessage());
        }



    }

}
