package com.runjian.cascade.gb28181.transmit.event.request.impl;

import com.runjian.cascade.gb28181.transmit.SIPProcessorObserver;
import com.runjian.cascade.gb28181.transmit.event.request.ISIPRequestProcessor;
import com.runjian.cascade.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * SIP命令类型： ACK请求
 */
@Component
@Slf4j
public class AckRequestProcessor extends SIPRequestProcessorParent implements InitializingBean, ISIPRequestProcessor {

	private final String method = "ACK";

	@Autowired
	private SIPProcessorObserver sipProcessorObserver;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 添加消息处理的订阅
		sipProcessorObserver.addRequestProcessor(method, this);
	}




	/**
	 * 处理  ACK请求
	 *
	 * @param evt
	 */
	@Override
	public void process(RequestEvent evt) {
		SIPRequest request = (SIPRequest) evt.getRequest();
		log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "SIP命令ack请求处理", "收到请求信息", request.toString());

		CallIdHeader callIdHeader = (CallIdHeader)evt.getRequest().getHeader(CallIdHeader.NAME);
		String platformGbId = ((SipURI) ((HeaderAddress) evt.getRequest().getHeader(FromHeader.NAME)).getAddress().getURI()).getUser();
		String channelId = ((SipURI) ((HeaderAddress) evt.getRequest().getHeader(ToHeader.NAME)).getAddress().getURI()).getUser();
		//判断之前invite中的信息是否存在 可以根据 callId+platformGbId+channelId 查找上一个事务中的点播信息

		//之前的事务点播信息失败 则直接return  不进行下一步

		//通知流媒体进行推流  下发PlatformInviteRtpItem
		

	}

}
