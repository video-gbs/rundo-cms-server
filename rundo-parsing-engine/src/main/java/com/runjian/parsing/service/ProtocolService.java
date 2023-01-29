package com.runjian.parsing.service;

import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.protocol.NorthProtocol;

import java.util.HashMap;

/**
 * @author Miracle
 * @date 2023/1/17 16:37
 */
public interface ProtocolService {

    /**
     * 协议集合
     */
    HashMap<String, NorthProtocol> PROTOCOL_MAP = new HashMap<>();

    /**
     * 初始化将所有协议的实现都加载进来
     */
    void init();

    /**
     * 获取协议
     * @param id 数据表id
     * @param idType {@link IdType}
     * @return 协议
     */
    NorthProtocol getProtocol(Long id, IdType idType);

    /**
     * 添加协议
     * @param protocolName 协议名称
     * @param northProtocol 协议
     */
    void addProtocol(String protocolName, NorthProtocol northProtocol);

}
