package com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.control.cmd;

import com.runjian.cascade.gb28181Module.common.enums.DeviceControlType;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.impl.SIPCommanderFroPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.IMessageHandler;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl.message.control.ControlMessageHandler;
import gov.nist.javax.sip.message.SIPRequest;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sip.RequestEvent;
import javax.sip.address.SipURI;

import static com.runjian.cascade.gb28181Module.gb28181.utils.XmlUtil.getText;


@Component
public class DeviceControlQueryMessageHandler extends SIPRequestProcessorParent implements InitializingBean, IMessageHandler {

    private Logger logger = LoggerFactory.getLogger(DeviceControlQueryMessageHandler.class);
    private final String cmdType = "DeviceControl";

    @Autowired
    private ControlMessageHandler controlMessageHandler;


    @Autowired
    private SIPCommanderFroPlatform cmderFroPlatform;

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        controlMessageHandler.addHandler(cmdType, this);
    }


    @Override
    public void handForPlatform(RequestEvent evt, ParentPlatform parentPlatform, Element rootElement) {

        SIPRequest request = (SIPRequest) evt.getRequest();

        String targetGBId = ((SipURI) request.getToHeader().getAddress().getURI()).getUser();
        String channelId = getText(rootElement, "DeviceID");
        // 远程启动功能
        if (!ObjectUtils.isEmpty(getText(rootElement, "TeleBoot"))) {
            // TODO 拒绝远程启动命令
            logger.warn("[国标级联]收到平台的远程启动命令， 不处理");

        DeviceControlType deviceControlType = DeviceControlType.typeOf(rootElement);
        logger.info("[接受deviceControl命令] 命令: {}", deviceControlType);
        if (!ObjectUtils.isEmpty(deviceControlType) && parentPlatform.getServerGbId().equals(targetGBId)) {
            //判断是否存在该通道

//            switch (deviceControlType) {
//                case PTZ:
//                    handlePtzCmd(deviceForPlatform, channelId, rootElement, request, DeviceControlType.PTZ);
//                    break;
//                case ALARM:
//                    handleAlarmCmd(deviceForPlatform, rootElement, request);
//                    break;
//                case GUARD:
//                    handleGuardCmd(deviceForPlatform, rootElement, request, DeviceControlType.GUARD);
//                    break;
//                case RECORD:
//                    handleRecordCmd(deviceForPlatform, channelId, rootElement, request, DeviceControlType.RECORD);
//                    break;
//                case I_FRAME:
//                    handleIFameCmd(deviceForPlatform, request, channelId);
//                    break;
//                case TELE_BOOT:
//                    handleTeleBootCmd(deviceForPlatform, request);
//                    break;
//                case DRAG_ZOOM_IN:
//                    handleDragZoom(deviceForPlatform, channelId, rootElement, request, DeviceControlType.DRAG_ZOOM_IN);
//                    break;
//                case DRAG_ZOOM_OUT:
//                    handleDragZoom(deviceForPlatform, channelId, rootElement, request, DeviceControlType.DRAG_ZOOM_OUT);
//                    break;
//                case HOME_POSITION:
//                    handleHomePositionCmd(deviceForPlatform, channelId, rootElement, request, DeviceControlType.HOME_POSITION);
//                    break;
//                default:
//                    break;
            }
        }
    }

}
