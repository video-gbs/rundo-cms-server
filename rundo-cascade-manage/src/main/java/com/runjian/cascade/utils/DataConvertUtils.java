package com.runjian.cascade.utils;

import com.runjian.cascade.constant.SipTransportType;
import com.runjian.cascade.entity.PlatformInfo;
import com.runjian.cascade.gb28181Module.gb28181.bean.OtherPlatform;

/**
 * @author Miracle
 * @date 2023/12/26 17:47
 */
public class DataConvertUtils {

    public static OtherPlatform toOtherPlatform(PlatformInfo platformInfo) {
        OtherPlatform otherPlatform = new OtherPlatform();
        otherPlatform.setName(platformInfo.getName());
        otherPlatform.setServerGbId(platformInfo.getGbCode());
        otherPlatform.setServerGbDomain(platformInfo.getGbCode().substring(0, Math.min(10, platformInfo.getGbCode().length())));
        otherPlatform.setServerIp(platformInfo.getIp());
        otherPlatform.setServerPort(platformInfo.getPort());
        otherPlatform.setUsername(platformInfo.getUsername());
        otherPlatform.setPassword(platformInfo.getPassword());
        otherPlatform.setTransport(SipTransportType.getSipTransportMsg(platformInfo.getSipTransport()));
        return otherPlatform;
    }

}
