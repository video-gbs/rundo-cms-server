package com.runjian.cascade.gb28181Module.gb28181;
import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.gb28181Module.gb28181.conf.DefaultProperties;
import com.runjian.cascade.gb28181Module.gb28181.transmit.ISIPProcessorObserver;
import com.runjian.common.constant.LogTemplate;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.SipStackImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sip.*;
import java.util.TooManyListenersException;

/**
 * @author chenjialing
 */
@Configuration
public class SipLayer{

    private final static Logger logger = LoggerFactory.getLogger(SipLayer.class);

    @Autowired
    private SipConfig sipConfig;

    @Autowired
    private ISIPProcessorObserver sipProcessorObserver;

    private SipStackImpl sipStack;

    private SipFactory sipFactory;


    @Bean("sipFactory")
    SipFactory createSipFactory() {
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        return sipFactory;
    }

    @Bean("sipStack")
    @DependsOn({"sipFactory"})
    SipStackImpl createSipStack() throws PeerUnavailableException {
        sipStack = ( SipStackImpl )sipFactory.createSipStack(DefaultProperties.getProperties(sipConfig.getMonitorIp(), false));
        return sipStack;
    }

    @Bean(name = "tcpSipProvider")
    @DependsOn("sipStack")
    SipProviderImpl startTcpListener() {
        ListeningPoint tcpListeningPoint = null;
        SipProviderImpl tcpSipProvider  = null;
        try {
            tcpListeningPoint = sipStack.createListeningPoint(sipConfig.getMonitorIp(), sipConfig.getPort(), "TCP");
            tcpSipProvider = (SipProviderImpl)sipStack.createSipProvider(tcpListeningPoint);
            tcpSipProvider.setDialogErrorsAutomaticallyHandled();
            tcpSipProvider.addSipListener(sipProcessorObserver);
            logger.info(LogTemplate.PROCESS_LOG_TEMPLATE, "Sip Server", String.format("TCP 启动成功 %s:%s",sipConfig.getMonitorIp(), sipConfig.getPort()));
        } catch (TransportNotSupportedException e) {
            logger.error(LogTemplate.ERROR_LOG_TEMPLATE, "Sip Server", "不支持的传输方式", e);
        } catch (InvalidArgumentException e) {
            logger.error(LogTemplate.PROCESS_LOG_TEMPLATE, "Sip Server", String.format("无法使用 [ %s:%s ]作为SIP[ TCP ]服务，可排查: 1. sip.monitor-ip 是否为本机网卡IP; 2. sip.port 是否已被占用", sipConfig.getMonitorIp(), sipConfig.getPort()));
        } catch (TooManyListenersException e) {
            logger.error(LogTemplate.ERROR_LOG_TEMPLATE, "Sip Server", "sip监听过多", e);
        } catch (ObjectInUseException e) {
            logger.error(LogTemplate.ERROR_LOG_TEMPLATE, "Sip Server", "对象正在使用", e);
        }
        return tcpSipProvider;
    }

    @Bean(name = "udpSipProvider")
    @DependsOn("sipStack")
    SipProviderImpl startUdpListener() {
        ListeningPoint udpListeningPoint = null;
        SipProviderImpl udpSipProvider = null;
        try {
            udpListeningPoint = sipStack.createListeningPoint(sipConfig.getMonitorIp(), sipConfig.getPort(), "UDP");
            udpSipProvider = (SipProviderImpl)sipStack.createSipProvider(udpListeningPoint);
            udpSipProvider.addSipListener(sipProcessorObserver);
        } catch (TransportNotSupportedException e) {
            logger.error(LogTemplate.ERROR_LOG_TEMPLATE, "Sip Server", "不支持的传输方式", e);
        } catch (InvalidArgumentException e) {
            logger.error(LogTemplate.PROCESS_LOG_TEMPLATE, "Sip Server", String.format("无法使用 [ %s:%s ]作为SIP[ TCP ]服务，可排查: 1. sip.monitor-ip 是否为本机网卡IP; 2. sip.port 是否已被占用", sipConfig.getMonitorIp(), sipConfig.getPort()));
        } catch (TooManyListenersException e) {
            logger.error(LogTemplate.ERROR_LOG_TEMPLATE, "Sip Server", "sip监听过多", e);
        } catch (ObjectInUseException e) {
            logger.error(LogTemplate.ERROR_LOG_TEMPLATE, "Sip Server", "对象正在使用", e);
        }
        logger.info(LogTemplate.PROCESS_LOG_TEMPLATE, "Sip Server", String.format("UDP 启动成功 %s:%s",sipConfig.getMonitorIp(), sipConfig.getPort()));
        return udpSipProvider;
    }

}