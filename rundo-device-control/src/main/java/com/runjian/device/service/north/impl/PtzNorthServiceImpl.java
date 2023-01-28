package com.runjian.device.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.service.north.PtzNorthService;
import com.runjian.device.vo.feign.PutPtzControlReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 云台控制服务
 * @author Miracle
 * @date 2023/1/9 14:15
 */

@Slf4j
@Service
public class PtzNorthServiceImpl implements PtzNorthService {

    @Autowired
    private ParsingEngineApi parsingEngineApi;

    /**
     * 云台控制状态
     * @param chId 通道ID
     * @param commandCode 指令code
     * @param horizonSpeed 水平速度
     * @param verticalSpeed 垂直速度
     * @param zoomSpeed 缩放速度
     * @param totalSpeed 总速度
     */
    @Override
    public void ptzControl(Long chId, Integer commandCode, Integer horizonSpeed, Integer verticalSpeed, Integer zoomSpeed, Integer totalSpeed) {
        PutPtzControlReq req = new PutPtzControlReq();
        req.setChannelId(chId);
        req.setCommandCode(commandCode);
        req.setHorizonSpeed(horizonSpeed);
        req.setVerticalSpeed(verticalSpeed);
        req.setZoomSpeed(zoomSpeed);
        req.setTotalSpeed(totalSpeed);
        CommonResponse<Boolean> response = parsingEngineApi.ptzControl(req);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "云台控制北向服务", "云台控制失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
    }
}
