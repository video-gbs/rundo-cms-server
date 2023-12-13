package com.runjian.cascade.service;

import com.github.pagehelper.PageInfo;
import com.runjian.cascade.vo.response.GetCascadeGatewayRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/12/11 15:06
 */
public interface CascadeGatewayService {

    /**
     * 获取级联网关
     */
    PageInfo<GetCascadeGatewayRsp> getCascadeGateway(int page, int num);

    /**
     * 级联网关上报
     * @param gatewayId
     * @param name
     * @param ip
     * @param port
     * @param gbDomain
     * @param gbCode
     * @param username
     * @param password
     */
    void cascadeGatewayReport(Long gatewayId, String name, String ip, Integer port, String gbDomain, String gbCode, String username, String password);




}
