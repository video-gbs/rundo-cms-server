package com.runjian.cascade.gb28181.transmit.event.request.impl.message;

import com.runjian.cascade.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181.transmit.event.request.SIPRequestProcessorParent;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sip.RequestEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.runjian.cascade.gb28181.utils.XmlUtil.getText;

public abstract class MessageHandlerAbstract extends SIPRequestProcessorParent implements IMessageHandler{

    public Map<String, IMessageHandler> messageHandlerMap = new ConcurrentHashMap<>();



    public void addHandler(String cmdType, IMessageHandler messageHandler) {
        messageHandlerMap.put(cmdType, messageHandler);
    }


    @Override
    public void handForPlatform(RequestEvent evt, ParentPlatform parentPlatform, Element element) {
        String cmd = getText(element, "CmdType");
        IMessageHandler messageHandler = messageHandlerMap.get(cmd);
        if (messageHandler != null) {
            messageHandler.handForPlatform(evt, parentPlatform, element);
        }
    }
}
