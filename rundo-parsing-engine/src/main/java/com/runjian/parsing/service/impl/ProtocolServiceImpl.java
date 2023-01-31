package com.runjian.parsing.service.impl;


import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.protocol.AbstractSouthProtocol;
import com.runjian.parsing.protocol.SouthProtocol;
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
        Map<String, NorthProtocol> northProtocolRealizeMap = applicationContext.getBeansOfType(NorthProtocol.class);
        for (NorthProtocol northProtocol : northProtocolRealizeMap.values()){
            NORTH_PROTOCOL_MAP.put(northProtocol.getProtocolName(), northProtocol);
        }

        Map<String, AbstractSouthProtocol> southProtocolRealizeMap = applicationContext.getBeansOfType(AbstractSouthProtocol.class);
        for (AbstractSouthProtocol southProtocol : southProtocolRealizeMap.values()){
            SOUTH_PROTOCOL_MAP.put(southProtocol.getProtocolName(), southProtocol);
        }
    }

    /**
     * 获取北向协议
     * @param id 数据表id
     * @param idType {@link IdType}
     * @return 协议
     */
    public NorthProtocol getNorthProtocol(Long id, IdType idType){
        switch (idType){
            case CHANNEL:
                id = dataBaseService.getChannelInfo(id).getDeviceId();
            case DEVICE:
                id = dataBaseService.getDeviceInfo(id).getGatewayId();
            case GATEWAY:
                return NORTH_PROTOCOL_MAP.getOrDefault(dataBaseService.getGatewayInfo(id).getProtocol(), NORTH_PROTOCOL_MAP.get(NorthProtocol.DEFAULT_PROTOCOL));
            default:
                return NORTH_PROTOCOL_MAP.get(NorthProtocol.DEFAULT_PROTOCOL);
        }
    }

    /**
     * 获取南向协议
     * @param id 数据表id
     * @param idType {@link IdType}
     * @return 协议
     */
    @Override
    public SouthProtocol getSouthProtocol(Long id, IdType idType) {
        switch (idType){
            case CHANNEL:
                id = dataBaseService.getChannelInfo(id).getDeviceId();
            case DEVICE:
                id = dataBaseService.getDeviceInfo(id).getGatewayId();
            case GATEWAY:
                return SOUTH_PROTOCOL_MAP.getOrDefault(dataBaseService.getGatewayInfo(id).getProtocol(), SOUTH_PROTOCOL_MAP.get(SouthProtocol.DEFAULT_PROTOCOL));
            default:
                return SOUTH_PROTOCOL_MAP.get(SouthProtocol.DEFAULT_PROTOCOL);
        }
    }

    /**
     * 添加协议
     * @param protocolName 协议名称
     * @param northProtocol 协议
     */
    public void addNorthProtocol(String protocolName, NorthProtocol northProtocol){
        synchronized (this){
            NORTH_PROTOCOL_MAP.put(protocolName, northProtocol);
        }
    }

    /**
     * 添加协议
     * @param protocolName 协议名称
     * @param southProtocol 协议
     */
    @Override
    public void addSouthProtocol(String protocolName, SouthProtocol southProtocol) {
        synchronized (this){
            SOUTH_PROTOCOL_MAP.put(protocolName, southProtocol);
        }
    }

}
