package com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.query.cmd;

import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181Module.gb28181.bean.RecordInfo;
import com.runjian.cascade.gb28181Module.gb28181.event.record.RecordEndEventListener;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.impl.SIPCommanderFroPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.IMessageHandler;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.query.QueryMessageHandler;
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
import java.text.ParseException;

@Component
@Slf4j
public class RecordInfoQueryMessageHandler extends SIPRequestProcessorParent implements InitializingBean, IMessageHandler {

    private Logger logger = LoggerFactory.getLogger(RecordInfoQueryMessageHandler.class);
    private final String cmdType = "RecordInfo";

    @Autowired
    private QueryMessageHandler queryMessageHandler;


    @Autowired
    private SIPCommanderFroPlatform cmderFroPlatform;

    @Autowired
    private RecordEndEventListener recordEndEventListener;

    @Autowired
    private SipConfig config;

    @Override
    public void afterPropertiesSet() throws Exception {
        queryMessageHandler.addHandler(cmdType, this);
    }



    @Override
    public void handForPlatform(RequestEvent evt, ParentPlatform parentPlatform, Element rootElement) {
        log.info(LogTemplate.PROCESS_LOG_TEMPLATE,"级联sip","录像信息查询",evt.getRequest());
        SIPRequest request = (SIPRequest) evt.getRequest();
        Element snElement = rootElement.element("SN");
        int sn = Integer.parseInt(snElement.getText());
        Element deviceIDElement = rootElement.element("DeviceID");
        String channelId = deviceIDElement.getText();
        Element startTimeElement = rootElement.element("StartTime");
        String startTime = null;
        if (startTimeElement != null) {
            startTime = startTimeElement.getText();
        }
        Element endTimeElement = rootElement.element("EndTime");
        String endTime = null;
        if (endTimeElement != null) {
            endTime = endTimeElement.getText();
        }
        Element secrecyElement = rootElement.element("Secrecy");
        int secrecy = 0;
        if (secrecyElement != null) {
            secrecy = Integer.parseInt(secrecyElement.getText().trim());
        }
        String type = "all";
        Element typeElement = rootElement.element("Type");
        if (typeElement != null) {
            type =  typeElement.getText();
        }
        //todo 通知下级进行的设备录像的查询 然后返回

        RecordInfo recordInfo = new RecordInfo();
        try {
            cmderFroPlatform.recordInfo(parentPlatform, request.getFromTag(), recordInfo);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            logger.error("[命令发送失败] 国标级联 回复录像数据: {}", e.getMessage());
        }
    }
}
