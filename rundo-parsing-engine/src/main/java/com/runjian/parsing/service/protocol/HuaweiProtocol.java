package com.runjian.parsing.service.protocol;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/1/17 18:27
 */
@Service
public class HuaweiProtocol extends DefaultProtocol {

    @Override
    public void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        // 设备添加，把信息发到网关，网关生成对应的操作id返回回来，然后记录原始id
    }

}
