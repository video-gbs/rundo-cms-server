package com.runjian.cascade.gb28181Module.gb28181.transmit.cmd;

import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181Module.gb28181.bean.PlatformInviteRtpItem;
import com.runjian.cascade.gb28181Module.gb28181.bean.SubscribeInfo;
import com.runjian.cascade.gb28181Module.gb28181.utils.SipUtils;
import com.runjian.cascade.gb28181Module.service.IRedisCatchStorageService;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @description: 平台命令request创造器 TODO 冗余代码太多待优化
 * @author: panll
 * @date: 2020年5月6日 上午9:29:02
 */
@Component
public class SIPRequestHeaderPlarformProvider {

	@Autowired
	private SipConfig sipConfig;
	
	@Autowired
	private SipFactory sipFactory;


	@Autowired
	private IRedisCatchStorageService redisCatchStorage;

	public Request createRegisterRequest(@NotNull ParentPlatform platform, long CSeq, String fromTag, String toTag, CallIdHeader callIdHeader, int expires) throws ParseException, InvalidArgumentException, PeerUnavailableException {
		Request request = null;
		String sipAddress = sipConfig.getIp() + ":" + sipConfig.getPort();
		//请求行
		SipURI requestLine = sipFactory.createAddressFactory().createSipURI(platform.getServerGbId(),
				platform.getServerIp() + ":" + platform.getServerPort());
		//via
		ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		ViaHeader viaHeader = SipFactory.getInstance().createHeaderFactory().createViaHeader(platform.getDeviceIp(),
				Integer.parseInt(platform.getDevicePort()), platform.getTransport(), SipUtils.getNewViaTag());
		viaHeader.setRPort();
		viaHeaders.add(viaHeader);
		//from
		SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(platform.getDeviceGbId(), sipConfig.getDomain());
		Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
		FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, fromTag);
		//to
		SipURI toSipURI = SipFactory.getInstance().createAddressFactory().createSipURI(platform.getDeviceGbId(), sipConfig.getDomain());
		Address toAddress = SipFactory.getInstance().createAddressFactory().createAddress(toSipURI);
		ToHeader toHeader = SipFactory.getInstance().createHeaderFactory().createToHeader(toAddress,toTag);

		//Forwards
		MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);

		//ceq
		CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(CSeq, Request.REGISTER);
		request = sipFactory.createMessageFactory().createRequest(requestLine, Request.REGISTER, callIdHeader,
				cSeqHeader,fromHeader, toHeader, viaHeaders, maxForwards);

		Address concatAddress = sipFactory.createAddressFactory().createAddress(sipFactory.createAddressFactory()
				.createSipURI(platform.getDeviceGbId(), sipAddress));
		request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));

		ExpiresHeader expiresHeader = sipFactory.createHeaderFactory().createExpiresHeader(expires);
		request.addHeader(expiresHeader);

		request.addHeader(SipUtils.createUserAgentHeader(sipFactory));

		return request;
	}

	public Request createRegisterRequest(@NotNull ParentPlatform parentPlatform, String fromTag, String toTag,
										 WWWAuthenticateHeader www , CallIdHeader callIdHeader, int expires) throws ParseException, PeerUnavailableException, InvalidArgumentException {


		Request registerRequest = createRegisterRequest(parentPlatform, redisCatchStorage.getCSEQ(), fromTag, toTag, callIdHeader, expires);
		SipURI requestURI = SipFactory.getInstance().createAddressFactory().createSipURI(parentPlatform.getServerGbId(), parentPlatform.getServerIp() + ":" + parentPlatform.getServerPort());
		if (www == null) {
			AuthorizationHeader authorizationHeader = SipFactory.getInstance().createHeaderFactory().createAuthorizationHeader("Digest");
			String username = parentPlatform.getUsername();
			if ( username == null || username == "" )
			{
				authorizationHeader.setUsername(parentPlatform.getDeviceGbId());
			} else {
				authorizationHeader.setUsername(username);
			}
			authorizationHeader.setURI(requestURI);
			authorizationHeader.setAlgorithm("MD5");
			registerRequest.addHeader(authorizationHeader);
			return  registerRequest;
		}
		String realm = www.getRealm();
		String nonce = www.getNonce();
		String scheme = www.getScheme();

		// 参考 https://blog.csdn.net/y673533511/article/details/88388138
		// qop 保护质量 包含auth（默认的）和auth-int（增加了报文完整性检测）两种策略
		String qop = www.getQop();

		String cNonce = null;
		String nc = "00000001";
		if (qop != null) {
			if ("auth".equalsIgnoreCase(qop)) {
				// 客户端随机数，这是一个不透明的字符串值，由客户端提供，并且客户端和服务器都会使用，以避免用明文文本。
				// 这使得双方都可以查验对方的身份，并对消息的完整性提供一些保护
				cNonce = UUID.randomUUID().toString();

			}else if ("auth-int".equalsIgnoreCase(qop)){
				// TODO
			}
		}
		String HA1 = DigestUtils.md5DigestAsHex((parentPlatform.getDeviceGbId() + ":" + realm + ":" + parentPlatform.getPassword()).getBytes());
		String HA2=DigestUtils.md5DigestAsHex((Request.REGISTER + ":" + requestURI.toString()).getBytes());

		StringBuffer reStr = new StringBuffer(200);
		reStr.append(HA1);
		reStr.append(":");
		reStr.append(nonce);
		reStr.append(":");
		if (qop != null) {
			reStr.append(nc);
			reStr.append(":");
			reStr.append(cNonce);
			reStr.append(":");
			reStr.append(qop);
			reStr.append(":");
		}
		reStr.append(HA2);

		String RESPONSE = DigestUtils.md5DigestAsHex(reStr.toString().getBytes());

		AuthorizationHeader authorizationHeader = SipFactory.getInstance().createHeaderFactory().createAuthorizationHeader(scheme);
		authorizationHeader.setUsername(parentPlatform.getDeviceGbId());
		authorizationHeader.setRealm(realm);
		authorizationHeader.setNonce(nonce);
		authorizationHeader.setURI(requestURI);
		authorizationHeader.setResponse(RESPONSE);
		authorizationHeader.setAlgorithm("MD5");
		if (qop != null) {
			authorizationHeader.setQop(qop);
			authorizationHeader.setCNonce(cNonce);
			authorizationHeader.setNonceCount(1);
		}
		registerRequest.addHeader(authorizationHeader);

		return registerRequest;
	}

	public Request createMessageRequest(ParentPlatform parentPlatform, String content, PlatformInviteRtpItem sendRtpItem) throws PeerUnavailableException, ParseException, InvalidArgumentException {
		CallIdHeader callIdHeader = sipFactory.createHeaderFactory().createCallIdHeader(sendRtpItem.getCallId());
		return createMessageRequest(parentPlatform, content, sendRtpItem.getToTag(), SipUtils.getNewViaTag(), sendRtpItem.getFromTag(), callIdHeader);
	}

	public Request createMessageRequest(ParentPlatform parentPlatform, String content, String fromTag, String viaTag, CallIdHeader callIdHeader) throws PeerUnavailableException, ParseException, InvalidArgumentException {
		return createMessageRequest(parentPlatform, content, fromTag, viaTag, null, callIdHeader);
	}


	public Request createMessageRequest(ParentPlatform parentPlatform, String content, String fromTag, String viaTag, String toTag, CallIdHeader callIdHeader) throws PeerUnavailableException, ParseException, InvalidArgumentException {
		Request request = null;
		String serverAddress = parentPlatform.getServerIp()+ ":" + parentPlatform.getServerPort();
		// sipuri
		SipURI requestURI = sipFactory.createAddressFactory().createSipURI(parentPlatform.getServerGbId(), serverAddress);
		// via
		ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		ViaHeader viaHeader = sipFactory.createHeaderFactory().createViaHeader(parentPlatform.getDeviceIp(), Integer.parseInt(parentPlatform.getDevicePort()),
				parentPlatform.getTransport(), viaTag);
		viaHeader.setRPort();
		viaHeaders.add(viaHeader);
		// from
		// SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(parentPlatform.getDeviceGbId(), parentPlatform.getDeviceIp() + ":" + parentPlatform.getDeviceIp());
		SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(parentPlatform.getDeviceGbId(), sipConfig.getDomain());
		Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
		FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, fromTag);
		// to
		SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(parentPlatform.getServerGbId(), serverAddress);
		Address toAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
		ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toAddress, toTag);

		// Forwards
		MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
		// ceq
		CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(redisCatchStorage.getCSEQ(), Request.MESSAGE);
		MessageFactoryImpl messageFactory = (MessageFactoryImpl) sipFactory.createMessageFactory();
		// 设置编码， 防止中文乱码
		messageFactory.setDefaultContentEncodingCharset(parentPlatform.getCharacterSet());
		request = messageFactory.createRequest(requestURI, Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
				toHeader, viaHeaders, maxForwards);

		request.addHeader(SipUtils.createUserAgentHeader(sipFactory));

		ContentTypeHeader contentTypeHeader = sipFactory.createHeaderFactory().createContentTypeHeader("Application", "MANSCDP+xml");
		request.setContent(content, contentTypeHeader);
		return request;
	}

	public SIPRequest createNotifyRequest(ParentPlatform parentPlatform, String content, SubscribeInfo subscribeInfo) throws PeerUnavailableException, ParseException, InvalidArgumentException {
		SIPRequest request = null;
		// sipuri
		SipURI requestURI = sipFactory.createAddressFactory().createSipURI(parentPlatform.getServerGbId(), parentPlatform.getServerIp()+ ":" + parentPlatform.getServerPort());
		// via
		ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
		ViaHeader viaHeader = sipFactory.createHeaderFactory().createViaHeader(parentPlatform.getDeviceIp(), Integer.parseInt(parentPlatform.getDevicePort()),
				parentPlatform.getTransport(), SipUtils.getNewViaTag());
		viaHeader.setRPort();
		viaHeaders.add(viaHeader);
		// from
		SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(parentPlatform.getDeviceGbId(),
				parentPlatform.getDeviceIp() + ":" + parentPlatform.getDevicePort());
		Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
		FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, subscribeInfo.getResponse().getToTag());
		// to
		SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(parentPlatform.getServerGbId(), parentPlatform.getServerGbDomain());
		Address toAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
		ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toAddress, subscribeInfo.getRequest().getFromTag());

		// Forwards
		MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
		// ceq
		CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(redisCatchStorage.getCSEQ(), Request.NOTIFY);
		MessageFactoryImpl messageFactory = (MessageFactoryImpl) sipFactory.createMessageFactory();
		// 设置编码， 防止中文乱码
		messageFactory.setDefaultContentEncodingCharset("gb2312");

		CallIdHeader callIdHeader = sipFactory.createHeaderFactory().createCallIdHeader(subscribeInfo.getRequest().getCallIdHeader().getCallId());

		request = (SIPRequest) messageFactory.createRequest(requestURI, Request.NOTIFY, callIdHeader, cSeqHeader, fromHeader,
				toHeader, viaHeaders, maxForwards);

		request.addHeader(SipUtils.createUserAgentHeader(sipFactory));

		EventHeader event = sipFactory.createHeaderFactory().createEventHeader(subscribeInfo.getEventType());
		if (subscribeInfo.getEventId() != null) {
			event.setEventId(subscribeInfo.getEventId());
		}

		request.addHeader(event);

		SubscriptionStateHeader active = sipFactory.createHeaderFactory().createSubscriptionStateHeader("active");
		request.setHeader(active);

		String sipAddress = sipConfig.getIp() + ":" + sipConfig.getPort();
		Address concatAddress = sipFactory.createAddressFactory().createAddress(sipFactory.createAddressFactory()
				.createSipURI(parentPlatform.getDeviceGbId(), sipAddress));
		request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));

		ContentTypeHeader contentTypeHeader = sipFactory.createHeaderFactory().createContentTypeHeader("Application", "MANSCDP+xml");
		request.setContent(content, contentTypeHeader);
		return request;
    }

	public SIPRequest createByeRequest(ParentPlatform platform, PlatformInviteRtpItem sendRtpItem) throws PeerUnavailableException, ParseException, InvalidArgumentException {

		if (sendRtpItem == null ) {
			return null;
		}

		SIPRequest request = null;
		// sipuri
		SipURI requestURI = sipFactory.createAddressFactory().createSipURI(platform.getServerGbId(), platform.getServerIp()+ ":" + platform.getServerPort());
		// via
		ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
		ViaHeader viaHeader = sipFactory.createHeaderFactory().createViaHeader(platform.getDeviceIp(), Integer.parseInt(platform.getDevicePort()),
				platform.getTransport(), SipUtils.getNewViaTag());
		viaHeader.setRPort();
		viaHeaders.add(viaHeader);
		// from
		SipURI fromSipURI = sipFactory.createAddressFactory().createSipURI(platform.getDeviceGbId(),
				platform.getDeviceIp() + ":" + platform.getDevicePort());
		Address fromAddress = sipFactory.createAddressFactory().createAddress(fromSipURI);
		FromHeader fromHeader = sipFactory.createHeaderFactory().createFromHeader(fromAddress, sendRtpItem.getToTag());
		// to
		SipURI toSipURI = sipFactory.createAddressFactory().createSipURI(platform.getServerGbId(), platform.getServerGbDomain());
		Address toAddress = sipFactory.createAddressFactory().createAddress(toSipURI);
		ToHeader toHeader = sipFactory.createHeaderFactory().createToHeader(toAddress, sendRtpItem.getFromTag());

		// Forwards
		MaxForwardsHeader maxForwards = sipFactory.createHeaderFactory().createMaxForwardsHeader(70);
		// ceq
		CSeqHeader cSeqHeader = sipFactory.createHeaderFactory().createCSeqHeader(redisCatchStorage.getCSEQ(), Request.BYE);
		MessageFactoryImpl messageFactory = (MessageFactoryImpl) sipFactory.createMessageFactory();
		// 设置编码， 防止中文乱码
		messageFactory.setDefaultContentEncodingCharset("gb2312");

		CallIdHeader callIdHeader = sipFactory.createHeaderFactory().createCallIdHeader(sendRtpItem.getCallId());

		request = (SIPRequest) messageFactory.createRequest(requestURI, Request.BYE, callIdHeader, cSeqHeader, fromHeader,
				toHeader, viaHeaders, maxForwards);

		request.addHeader(SipUtils.createUserAgentHeader(sipFactory));

		String sipAddress = sipConfig.getIp() + ":" + sipConfig.getPort();
		Address concatAddress = sipFactory.createAddressFactory().createAddress(sipFactory.createAddressFactory()
				.createSipURI(platform.getDeviceGbId(), sipAddress));
		request.addHeader(sipFactory.createHeaderFactory().createContactHeader(concatAddress));

		return request;
	}
}
