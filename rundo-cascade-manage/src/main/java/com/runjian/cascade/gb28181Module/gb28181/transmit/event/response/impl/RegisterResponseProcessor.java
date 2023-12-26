package com.runjian.cascade.gb28181Module.gb28181.transmit.event.response.impl;

import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.dao.PlatformMapper;
import com.runjian.cascade.entity.PlatformInfo;
import com.runjian.cascade.gb28181Module.common.constant.SipBusinessConstants;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181Module.gb28181.bean.PlatformRegisterInfo;
import com.runjian.cascade.gb28181Module.gb28181.bean.SipTransactionInfo;
import com.runjian.cascade.gb28181Module.gb28181.conf.DynamicTask;
import com.runjian.cascade.gb28181Module.gb28181.transmit.SIPProcessorObserver;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.event.response.SIPResponseProcessorAbstract;
import com.runjian.cascade.gb28181Module.service.IPlatformCommandService;
import com.runjian.cascade.gb28181Module.service.IRedisCatchStorageService;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

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
	PlatformMapper platformMapper;

	@Autowired
	private SIPProcessorObserver sipProcessorObserver;

	@Autowired
	private IRedisCatchStorageService redisCatchStorage;

	@Autowired
	SipConfig sipConfig;

	@Autowired
	private DynamicTask dynamicTask;

	private ConcurrentHashMap<String,Integer> keepAliveCountMap;

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
		String platformId = platformRegisterInfo.getPlatformId();
		PlatformInfo platformInfo = platformMapper.selectByGbCode(platformId);
		if(ObjectUtils.isEmpty(platformInfo)){
			log.info(LogTemplate.ERROR_LOG_TEMPLATE,"国标级联","平台信息未查询到",callId);
			return;
		}
		ConcurrentHashMap<String, CountDownLatch> locks = IPlatformCommandService.locks;
		ParentPlatform parentPlatform = new ParentPlatform();
		parentPlatform.setName(platformInfo.getName());
		parentPlatform.setServerGbId(platformInfo.getGbCode());
		sipConfig.completePlatfrom(parentPlatform);

		if (response.getStatusCode() == Response.UNAUTHORIZED) {
			WWWAuthenticateHeader www = (WWWAuthenticateHeader)response.getHeader(WWWAuthenticateHeader.NAME);
			SipTransactionInfo sipTransactionInfo = new SipTransactionInfo(response);
			try {
				sipCommanderForPlatform.register(parentPlatform, sipTransactionInfo, www, null, null, platformRegisterInfo.isRegister());
			} catch (SipException | InvalidArgumentException | ParseException e) {
				log.error("[命令发送失败] 国标级联 再次注册: {}", e.getMessage());
			}
		}else if (response.getStatusCode() == Response.OK){

			// 注册/注销成功移除缓存的信息
			redisCatchStorage.delPlatformRegisterInfo(callId);
		}
		boolean register = platformRegisterInfo.isRegister();
		String key = "";

		SipTransactionInfo sipTransactionInfo;
		if(register){
			//注册
			key = platformId+ SipBusinessConstants.PLATFORM_REGISTER_LOCK_PREFIX;
			sipTransactionInfo = new SipTransactionInfo(response);
			//成功  启动定时注册周期  以及进行定时心跳发送
			registerTask(parentPlatform,sipTransactionInfo);
			keepaliveTask(parentPlatform);
			redisCatchStorage.updatePlatformRegisterSip(parentPlatform.getServerGbId(),sipTransactionInfo);


		}else {
            //注销
			key = platformId+SipBusinessConstants.PLATFORM_UNREGISTER_LOCK_PREFIX;
			keepaliveTaskCancle(parentPlatform);
			registerTaskCancle(parentPlatform);
			redisCatchStorage.delPlatformRegisterSip(parentPlatform.getServerGbId());

		}
		CountDownLatch latch = locks.remove(key);
		if (latch != null) {
			latch.countDown();
		}


	}

	private void registerTask(ParentPlatform parentPlatform, SipTransactionInfo sipTransactionInfo){

		final String registerTaskKey = SipBusinessConstants.REGISTER_TASK_KEY_PREFIX + parentPlatform.getServerGbId();
		if (!dynamicTask.isAlive(registerTaskKey)) {
			// 添加注册任务
			dynamicTask.startCron(registerTaskKey,
					// 注册失败（注册成功时由程序直接调用了online方法）
					()-> {
						try {
							// 不在同一个会话中续订则每次全新注册
							if (sipTransactionInfo == null) {
								log.info("[国标级联] 平台：{}注册即将到期，开始重新注册", parentPlatform.getServerGbId());
							}else {
								log.info("[国标级联] 平台：{}注册即将到期，开始续订", parentPlatform.getServerGbId());
							}

							sipCommanderForPlatform.register(parentPlatform, sipTransactionInfo,  eventResult -> {
								log.info("[国标级联] 平台：{}注册失败，{}:{}", parentPlatform.getServerGbId(),
										eventResult.statusCode, eventResult.msg);
								//平台下线
								platformMapper.updateStatusByGbCode(parentPlatform.getServerGbId(),0);
							}, null);
						} catch (Exception e) {
							log.error("[命令发送失败] 国标级联定时注册: {}", e.getMessage());
						}
					},
					parentPlatform.getExpires() * 1000);
		}


	}

	private void keepaliveTask( ParentPlatform parentPlatform){
		String keepaliveTaskKey = SipBusinessConstants.KEEPALIVE_TASK_KEY_PREFIX + parentPlatform.getServerGbId();
		if (!dynamicTask.contains(keepaliveTaskKey)) {
			log.info("[国标级联]：{}, 添加定时心跳任务", parentPlatform.getServerGbId());
			// 添加心跳任务
			dynamicTask.startCron(keepaliveTaskKey,
					()-> {
						try {
							sipCommanderForPlatform.keepalive(parentPlatform, eventResult -> {
								// 心跳失败

								Integer errorCount = keepAliveCountMap.get(keepaliveTaskKey);
								if(errorCount == 2){
									// 设置平台离线，并重新注册
									platformMapper.updateStatusByGbCode(parentPlatform.getServerGbId(),0);
								}else {
									keepAliveCountMap.put(keepaliveTaskKey,errorCount+1);
								}

							}, eventResult -> {
								// 心跳成功
								// 清空之前的心跳超时计数
								Integer errorCount = keepAliveCountMap.get(keepaliveTaskKey);
								keepAliveCountMap.put(keepaliveTaskKey,0);

							});
						} catch (SipException | InvalidArgumentException | ParseException e) {
							log.error("[命令发送失败] 国标级联 发送心跳: {}", e.getMessage());
						}
					},
					(sipConfig.getKeepaliveTimeOut())*1000);
		}
	}

	private void registerTaskCancle(ParentPlatform parentPlatform){
		final String registerTaskKey = SipBusinessConstants.REGISTER_TASK_KEY_PREFIX + parentPlatform.getServerGbId();
		dynamicTask.stop(registerTaskKey);

	}

	private void keepaliveTaskCancle(ParentPlatform parentPlatform){
		String keepaliveTaskKey = SipBusinessConstants.KEEPALIVE_TASK_KEY_PREFIX + parentPlatform.getServerGbId();
		dynamicTask.stop(keepaliveTaskKey);
	}


}
