package com.runjian.stream.service.north;


import java.util.Set;

/**
 * 网关与调度服务绑定
 * @author Miracle
 * @date 2023/2/3 10:34
 */
public interface GatewayDispatchNorthService {

    void Binding(Long gatewayId, Set<Long> dispatchIds);
}
