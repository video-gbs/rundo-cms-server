package com.runjian.cascade.gb28181.transmit.event.request.impl.message;

import com.runjian.cascade.gb28181.bean.ParentPlatform;
import org.dom4j.Element;

import javax.sip.RequestEvent;

public interface IMessageHandler {

    /**
     * 处理来自平台的信息
     * @param evt
     * @param parentPlatform
     */
    void handForPlatform(RequestEvent evt, ParentPlatform parentPlatform, Element element);
}
