package com.runjian.stream.service.north;


import com.github.pagehelper.PageInfo;
import com.runjian.stream.vo.response.GetGatewayByIdsRsp;

import java.util.Set;

/**
 * 网关与调度服务绑定
 * @author Miracle
 * @date 2023/2/3 10:34
 */
public interface GatewayDispatchNorthService {

    /**
     * 获取绑定指定网关的调度服务id
     * @param gatewayId 网关id
     * @return
     */
    Long getDispatchIdByGatewayId(Long gatewayId);

    /**
     * 获取已绑定指定调度服务的网关
     * @param dispatchId 调度服务id
     * @return
     */
    PageInfo<GetGatewayByIdsRsp> getGatewayBindingDispatchId(int page, int num, Long dispatchId, String name);

    /**
     * 获取未绑定指定调度服务的网关
     * @param dispatchId
     * @return
     */
    PageInfo<GetGatewayByIdsRsp> getGatewayNotBindingDispatchId(int page, int num, Long dispatchId, String name);

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

    /**
     * 流媒体解除绑定网关
     * @param dispatchId 调度服务id
     * @param gatewayIds 网关id
     */
    void dispatchUnBindingGateway(Long dispatchId, Set<Long> gatewayIds);
}
