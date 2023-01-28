package com.runjian.parsing.service.impl;


import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.service.DataBaseService;
import com.runjian.parsing.service.ProtocolService;
import com.runjian.parsing.protocol.NorthProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 协议服务
 * @author Miracle
 * @date 2023/1/17 14:45
 */
@Service
public class ProtocolServiceImpl implements ProtocolService {

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 将所有协议的实现都加载进来
     */
    @PostConstruct
    public void init(){
        Map<String, NorthProtocol> protocolRealizeMap = applicationContext.getBeansOfType(NorthProtocol.class);
        for (NorthProtocol northProtocol : protocolRealizeMap.values()){
            PROTOCOL_MAP.put(northProtocol.getProtocolName(), northProtocol);
        }
    }

    /**
     * 获取协议
     * @param id 数据表id
     * @param idType {@link IdType}
     * @return 协议
     */
    public NorthProtocol getProtocol(Long id, IdType idType){
        switch (idType){
            case CHANNEL:
                id = dataBaseService.getChannelInfo(id).getDeviceId();
            case DEVICE:
                id = dataBaseService.getDeviceInfo(id).getGatewayId();
            case GATEWAY:
                return PROTOCOL_MAP.get( dataBaseService.getGatewayInfo(id).getProtocol());
            default:
                return PROTOCOL_MAP.get(NorthProtocol.DEFAULT_PROTOCOL);
        }
    }

    /**
     * 添加协议
     * @param protocolName 协议名称
     * @param northProtocol 协议
     */
    public void addProtocol(String protocolName, NorthProtocol northProtocol){
        synchronized (this){
            PROTOCOL_MAP.put(protocolName, northProtocol);
        }
    }

}
