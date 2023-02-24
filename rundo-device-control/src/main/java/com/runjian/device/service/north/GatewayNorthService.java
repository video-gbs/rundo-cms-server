package com.runjian.device.service.north;


import com.github.pagehelper.PageInfo;
import com.runjian.device.vo.response.GetGatewayByIdsRsp;
import com.runjian.device.vo.response.GetGatewayNameRsp;
import com.runjian.device.vo.response.GetGatewayPageRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/6 17:19
 */
public interface GatewayNorthService {

    /**
     * 获取网关所有名称
     * @return
     */
    List<GetGatewayNameRsp> getGatewayNameList();

    /**
     * 获取网关信息
     * @param page 页码
     * @param num 每页数量
     * @param name 网关名称
     * @return
     */
    PageInfo<GetGatewayPageRsp> getGatewayByPage(int page, int num, String name);

    /**
     * 修改网关信息
     * @param gatewayId
     * @param name
     */
    void updateGateway(Long gatewayId, String name);

    /**
     * 根据网关id获取网关信息
     * @param gatewayIds 网关id数组
     * @param isIn 包含还是不包含
     * @return
     */
    PageInfo<GetGatewayByIdsRsp> getGatewayByIds(int page, int num, List<Long> gatewayIds, Boolean isIn, String name);

}
