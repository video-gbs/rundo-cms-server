package com.runjian.device.service.north;


import com.runjian.device.vo.response.GetGatewayNameRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/6 17:19
 */
public interface GatewayNorthService {

    List<GetGatewayNameRsp> getGatewayNameList();
}
