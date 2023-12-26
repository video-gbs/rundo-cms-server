package com.runjian.cascade.gb28181.transmit.event.response.impl;

import com.runjian.cascade.gb28181.bean.PlatformRegisterInfo;
import com.runjian.cascade.gb28181.bean.SipTransactionInfo;
import com.runjian.cascade.gb28181.transmit.SIPProcessorObserver;
import com.runjian.cascade.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.runjian.cascade.gb28181.transmit.event.response.SIPResponseProcessorAbstract;
import com.runjian.cascade.service.IPlatformCommandService;
import com.runjian.cascade.service.IRedisCatchStorageService;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

//import static com.runjian.cascade.service.IPlatformCommandService.locks;

/**    
 * @description:Register响应处理器
 * @author: swwheihei
 * @date:   2020年5月3日 下午5:32:23     
 */
@Component
@Slf4j
public class RegisterResponseProcessor extends SIPResponseProcessorAbstract {

	private final String method = "REGISTER";

	@Autowired
	private ISIPCommanderForPlatform sipCommanderForPlatform;



	@Autowired
	private SIPProcessorObserver sipProcessorObserver;

	@Autowired
	private IRedisCatchStorageService redisCatchStorage;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 添加消息处理的订阅
		sipProcessorObserver.addResponseProcessor(method, this);
	}

	/**
	 * 处理Register响应
	 *
 	 * @param evt 事件
	 */
	@Override
	public void process(ResponseEvent evt) {
		SIPResponse response = (SIPResponse)evt.getResponse();
		String callId = response.getCallIdHeader().getCallId();
		PlatformRegisterInfo platformRegisterInfo = redisCatchStorage.queryPlatformRegisterInfo(callId);
		if (platformRegisterInfo == null) {
			log.info(LogTemplate.ERROR_LOG_TEMPLATE,"国标级联","注册请求未找到callId",callId);
			return;
		}

		ConcurrentHashMap<String, CountDownLatch> locks = IPlatformCommandService.locks;

		String key = "";
		CountDownLatch latch = locks.remove(key);
		if (latch != null) {
			latch.countDown();
		}
	}

}
