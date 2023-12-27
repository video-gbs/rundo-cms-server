package com.runjian.cascade.conf;


import cn.hutool.core.bean.BeanUtil;
import com.runjian.cascade.constant.SipTransportType;
import com.runjian.cascade.entity.PlatformInfo;
import com.runjian.cascade.gb28181Module.gb28181.bean.OtherPlatform;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sip", ignoreInvalidFields = true)
@Data
public class SipConfig {

	private String ip;

	/**
	 * 默认使用 0.0.0.0
	 */
	private String monitorIp = "0.0.0.0";

	private Integer port;

	private String domain;

	private String id;

	private String password;
	
	Integer ptzSpeed = 50;

	Integer keepaliveTimeOut = 255;

	Integer registerTimeInterval = 120;

	/**
	 * 订阅周期
	 */
	private int subscribeCatalogCycle = 600;

	/**
	 *字符编码
	 */
	private String characterSet;

	Integer expires = 300;

	public void completePlatfrom(ParentPlatform parentPlatform){
		parentPlatform.setCharacterSet(characterSet);
		parentPlatform.setExpires(expires);
		parentPlatform.setDeviceGbId(id);
		parentPlatform.setDeviceIp(ip);
		parentPlatform.setDevicePort(port);

	}

	public ParentPlatform convertPlatformDbSip(PlatformInfo platformInfo){
		ParentPlatform parentPlatform = new ParentPlatform();
		parentPlatform.setName(platformInfo.getName());
		parentPlatform.setServerGbId(platformInfo.getGbCode());
		parentPlatform.setTransport(SipTransportType.getSipTransportMsg(platformInfo.getSipTransport()));
		completePlatfrom(parentPlatform);
		return parentPlatform;

	}

}
