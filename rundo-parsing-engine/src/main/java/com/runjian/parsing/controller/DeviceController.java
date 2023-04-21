package com.runjian.parsing.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.common.constant.IdType;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.service.common.ProtocolService;
import com.runjian.parsing.vo.request.DeviceControlReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Set;

/**
 * 设备控制控制器
 * @author Miracle
 * @date 2023/1/12 10:00
 */
@RestController
@RequestMapping("/device-control")
@RequiredArgsConstructor
public class DeviceController {

    private final ProtocolService protocolService;

    private final ValidatorService validatorService;


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
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(req.getOutTime());
        protocolService.getNorthProtocol(req.getMainId(), IdType.getByCode(req.getIdType())).msgDistribute(MsgType.getByStr(req.getMsgType()), req.getMainId(), IdType.getByCode(req.getIdType()), req.getDataMap(), response);
        return response;
    }



}
