package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.service.IPlayService;
import com.runjian.device.expansion.service.IPtzService;
import com.runjian.device.expansion.vo.feign.request.FeignPtzControlReq;
import com.runjian.device.expansion.vo.feign.request.PlayBackFeignReq;
import com.runjian.device.expansion.vo.feign.request.PlayFeignReq;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import com.runjian.device.expansion.vo.request.PlayBackReq;
import com.runjian.device.expansion.vo.request.PlayReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class PtzServiceImpl implements IPtzService {

    @Autowired
    DeviceControlApi deviceControlApi;


    @Override
    public CommonResponse<?> ptzOperation(ChannelPtzControlReq request) {
        FeignPtzControlReq feignPtzControlReq = new FeignPtzControlReq();
        feignPtzControlReq.setChannelId(request.getChannelExpansionId());
        feignPtzControlReq.setCmdCode(request.getPtzOperationType());
        feignPtzControlReq.setCmdValue(request.getOperationValue());
        CommonResponse<?> commonResponse = deviceControlApi.ptzControl(feignPtzControlReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;

    }
}
