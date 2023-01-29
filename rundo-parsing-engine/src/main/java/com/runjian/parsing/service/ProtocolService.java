package com.runjian.parsing.service;

import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.protocol.NorthProtocol;
import com.runjian.parsing.protocol.SouthProtocol;

import java.util.HashMap;

/**
 * @author Miracle
 * @date 2023/1/17 16:37
 */
public interface ProtocolService {

    /**
     * 北向协议集合
     */
    HashMap<String, NorthProtocol> NORTH_PROTOCOL_MAP = new HashMap<>();

    /**
     * 南向协议集合
     */
    HashMap<String, SouthProtocol> SOUTH_PROTOCOL_MAP = new HashMap<>();

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
    NorthProtocol getNorthProtocol(Long id, IdType idType);

    /**
     * 获取协议
     * @param id 数据表id
     * @param idType {@link IdType}
     * @return 协议
     */
    SouthProtocol getSouthProtocol(Long id, IdType idType);

    /**
     * 添加北向协议
     * @param protocolName 协议名称
     * @param northProtocol 协议
     */
    void addNorthProtocol(String protocolName, NorthProtocol northProtocol);

    /**
     * 添加南向协议
     * @param protocolName 协议名称
     * @param southProtocol 协议
     */
    void addSouthProtocol(String protocolName, SouthProtocol southProtocol);

}
