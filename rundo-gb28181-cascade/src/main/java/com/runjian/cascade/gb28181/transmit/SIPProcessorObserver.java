package com.runjian.cascade.gb28181.transmit;

import com.runjian.cascade.gb28181.transmit.event.request.ISIPRequestProcessor;
import com.runjian.cascade.gb28181.transmit.event.response.ISIPResponseProcessor;
import com.runjian.common.constant.LogTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sip.*;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: SIP信令处理类观察者
 * @author: panlinlin
 * @date:   2021年11月5日 下午15：32
 */
@Component
public class SIPProcessorObserver implements ISIPProcessorObserver {

    private final static Logger logger = LoggerFactory.getLogger(SIPProcessorObserver.class);

    private static Map<String, ISIPRequestProcessor> requestProcessorMap = new ConcurrentHashMap<>();
    private static Map<String, ISIPResponseProcessor> responseProcessorMap = new ConcurrentHashMap<>();

    /**
     * 添加 request订阅
     * @param method 方法名
     * @param processor 处理程序
     */
    public void addRequestProcessor(String method, ISIPRequestProcessor processor) {
        requestProcessorMap.put(method, processor);
    }

    /**
     * 添加 response订阅
     * @param method 方法名
     * @param processor 处理程序
     */
    public void addResponseProcessor(String method, ISIPResponseProcessor processor) {
        responseProcessorMap.put(method, processor);
    }

    /**
     * 分发RequestEvent事件
     * @param requestEvent RequestEvent事件
     */
    @Override
    @Async("taskExecutor")
    public void processRequest(RequestEvent requestEvent) {
        String method = requestEvent.getRequest().getMethod();
        ISIPRequestProcessor sipRequestProcessor = requestProcessorMap.get(method);
        if (sipRequestProcessor == null) {
            logger.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "SIP信令处理类观察者", "不支持方法{}的request", method);

            return;
        }
        requestProcessorMap.get(method).process(requestEvent);

    }

    /**
     * 分发ResponseEvent事件
     * @param responseEvent responseEvent事件
     */
    @Override
    @Async("taskExecutor")
    public void processResponse(ResponseEvent responseEvent) {
        Response response = responseEvent.getResponse();
        int status = response.getStatusCode();

        // Success
        if (((status >= Response.OK) && (status < Response.MULTIPLE_CHOICES)) || status == Response.UNAUTHORIZED) {

        } else if ((status >= Response.TRYING) && (status < Response.OK)) {
            // 增加其它无需回复的响应，如101、180等
        } else {

        }


    }

    /**
     * 向超时订阅发送消息
     * @param timeoutEvent timeoutEvent事件
     */
    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        logger.info(LogTemplate.PROCESS_LOG_TEMPLATE, "SIP信令处理类观察者", "消息发送超时");



    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        System.out.println("processIOException");
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        CallIdHeader callId = dialogTerminatedEvent.getDialog().getCallId();
    }


}
