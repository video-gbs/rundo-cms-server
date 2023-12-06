package com.runjian.cascade.gb28181.transmit.event.request.impl.message.query.cmd;

import com.runjian.cascade.gb28181.bean.Device;
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
public class DeviceInfoQueryMessageHandler extends SIPRequestProcessorParent implements InitializingBean, IMessageHandler {

    private final String cmdType = "DeviceInfo";

    @Autowired
    private QueryMessageHandler queryMessageHandler;

    @Autowired
    private SIPCommanderFroPlatform cmderFroPlatform;

    @Override
    public void afterPropertiesSet() throws Exception {
        queryMessageHandler.addHandler(cmdType, this);
    }


    @Override
    public void handForPlatform(RequestEvent evt, ParentPlatform parentPlatform, Element rootElement) {
        log.info(LogTemplate.PROCESS_LOG_TEMPLATE,"级联sip","平台信息查询",evt.getRequest());
        FromHeader fromHeader = (FromHeader) evt.getRequest().getHeader(FromHeader.NAME);
        try {
            // 回复200 OK
            responseAck((SIPRequest) evt.getRequest(), Response.OK);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] DeviceInfo查询回复: {}", e.getMessage());
            return;
        }
        String sn = rootElement.element("SN").getText();

        /*根据WVP原有的数据结构，设备和通道是分开放置，设备信息都是存放在设备表里，通道表里的设备信息不可作为真实信息处理
        大部分NVR/IPC设备对他的通道信息实现都是返回默认的值没有什么参考价值。NVR/IPC通道我们统一使用设备表的设备信息来作为返回。
        我们这里使用查询数据库的方式来实现这个设备信息查询的功能，在其他地方对设备信息更新达到正确的目的。*/

        String channelId = getText(rootElement, "DeviceID");
        try {
            cmderFroPlatform.deviceInfoResponse(parentPlatform, sn, fromHeader.getTag());
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] 国标级联 DeviceInfo查询回复: {}", e.getMessage());
        }
    }
}
