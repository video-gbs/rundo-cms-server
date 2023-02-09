package com.runjian.stream.service.north;


import java.util.Set;

/**
 * 网关与调度服务绑定
 * @author Miracle
 * @date 2023/2/3 10:34
 */
public interface GatewayDispatchNorthService {

    /**
     * 网关绑定流媒体
     * @param gatewayId 网关id
     * @param dispatchId 调度服务id
     */
    void gatewayBindingDispatch(Long gatewayId, Long dispatchId);


    /**
     * 流媒体绑定网关
     * @param dispatchId 调度服务id
     * @param gatewayIds 网关id
     */
    void dispatchBindingGateway(Long dispatchId, Set<Long> gatewayIds);
}
