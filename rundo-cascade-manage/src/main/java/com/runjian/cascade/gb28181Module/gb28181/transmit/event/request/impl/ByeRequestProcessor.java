package com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.impl;

import com.runjian.cascade.gb28181Module.gb28181.transmit.SIPProcessorObserver;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.ISIPRequestProcessor;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderAddress;
import javax.sip.header.ToHeader;
import javax.sip.message.Response;
import java.text.ParseException;

/**
 * SIP命令类型： BYE请求
 */
@Component
@Slf4j
public class ByeRequestProcessor extends SIPRequestProcessorParent implements InitializingBean, ISIPRequestProcessor {

	private final String method = "BYE";



	@Autowired
	private SIPProcessorObserver sipProcessorObserver;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 添加消息处理的订阅
		sipProcessorObserver.addRequestProcessor(method, this);
	}

	/**
	 * 处理BYE请求
	 * @param evt
	 */
	@Override
	public void process(RequestEvent evt) {

		try {
			responseAck((SIPRequest) evt.getRequest(), Response.OK);
		} catch (SipException | InvalidArgumentException | ParseException e) {
			log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "SIP命令BYE请求处理", "回复BYE信息失败",this.getClass().getName(), e);
		}
		//进行点播信息事务的查找
		CallIdHeader callIdHeader = (CallIdHeader)evt.getRequest().getHeader(CallIdHeader.NAME);
		String platformGbId = ((SipURI) ((HeaderAddress) evt.getRequest().getHeader(FromHeader.NAME)).getAddress().getURI()).getUser();
		String channelId = ((SipURI) ((HeaderAddress) evt.getRequest().getHeader(ToHeader.NAME)).getAddress().getURI()).getUser();
		//判断之前invite中的信息是否存在 可以根据 callId+platformGbId+channelId 查找上一个事务中的点播信息

		//不存在则不用管理 可能是重发的bye请求

		//来自上级的主动bye请求
	}
}
