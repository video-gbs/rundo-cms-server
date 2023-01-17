package com.runjian.parsing.service.protocol;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.service.DataBaseService;
import com.runjian.parsing.service.TaskService;
import com.runjian.parsing.service.impl.ProtocolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * @author Miracle
 * @date 2023/1/17 14:39
 */
@Service
public class Gb28181Protocol extends DefaultProtocol {

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private TaskService taskService;

    @Override
    public String getProtocolName() {
        return "GB28181";
    }

    @Override
    public void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        dataBaseService.getGatewayInfo(gatewayId);
        DeviceInfo deviceInfo = new DeviceInfo();
        LocalDateTime nowTime = LocalDateTime.now();
        deviceInfo.setOriginId(dataMap.get("deviceId").toString());
        deviceInfo.setGatewayId(gatewayId);
        deviceInfo.setCreateTime(nowTime);
        deviceInfo.setUpdateTime(nowTime);
        deviceMapper.save(deviceInfo);
        response.setResult(CommonResponse.success(deviceInfo.getId()));
    }
}
