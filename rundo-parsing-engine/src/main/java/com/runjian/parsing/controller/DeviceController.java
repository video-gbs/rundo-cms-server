package com.runjian.parsing.controller;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.parsing.constant.IdType;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.service.common.ProtocolService;
import com.runjian.parsing.vo.request.DeviceControlReq;
import com.runjian.parsing.vo.request.DeviceCustomEventReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;
import java.util.Set;

/**
 * 设备控制控制器
 * @author Miracle
 * @date 2023/1/12 10:00
 */
@RestController
@RequestMapping("/device-control")
public class DeviceController {

    private static final long OUT_TIME = 10000L;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private ValidatorService validatorService;


    /**
     * 设备全量同步
     * @param gatewayIds
     * @return
     */
    @GetMapping("/device/total-sync")
    public CommonResponse<?> gatewayTotalSync(@RequestParam Set<Long> gatewayIds){
        for (Long gatewayId : gatewayIds){
            protocolService.getNorthProtocol(gatewayId, IdType.GATEWAY).msgDistribute(MsgType.DEVICE_TOTAL_SYNC, gatewayId, IdType.GATEWAY, null, null);
        }
        return CommonResponse.success();
    }


    /**
     * 自定义扩展事件处理
     * @param req 设备请求体
     * @return
     */
    @PostMapping("/custom/event")
    public DeferredResult<CommonResponse<?>> customEvent(@RequestBody DeviceControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        if (Objects.nonNull(req.getGatewayId())){
            protocolService.getNorthProtocol(req.getGatewayId(), IdType.GATEWAY).msgDistribute(MsgType.getByStr(req.getMsgType()), req.getGatewayId(), IdType.GATEWAY, req.getDataMap(), response);
        } else if (Objects.nonNull(req.getDeviceId())) {
            protocolService.getNorthProtocol(req.getDeviceId(), IdType.DEVICE).msgDistribute(MsgType.getByStr(req.getMsgType()), req.getDeviceId(), IdType.DEVICE, req.getDataMap(), response);
        } else if (Objects.nonNull(req.getChannelId())) {
            protocolService.getNorthProtocol(req.getChannelId(), IdType.CHANNEL).msgDistribute(MsgType.getByStr(req.getMsgType()), req.getChannelId(), IdType.CHANNEL, req.getDataMap(), response);
        }else {
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "主键id不能为空");
        }
        return response;
    }



}
