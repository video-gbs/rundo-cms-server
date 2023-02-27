package com.runjian.parsing.controller;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.service.common.ProtocolService;
import com.runjian.parsing.service.south.GatewayService;
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
     *
     * @param gatewayIds
     * @return
     */
    @GetMapping("/device/total-sync")
    public CommonResponse<?> gatewayTotalSync(@RequestParam Set<Long> gatewayIds){
        for (Long gatewayId : gatewayIds){
            protocolService.getNorthProtocol(gatewayId, IdType.GATEWAY).deviceTotalSync(gatewayId);
        }
        return CommonResponse.success();
    }

    /**
     * 设备信息同步
     * @param deviceId 设备id
     * @return 设备同步消息体
     */
    @GetMapping("/device/sync")
    public DeferredResult<CommonResponse<?>> deviceSync(@RequestParam Long deviceId){
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(deviceId, IdType.DEVICE).deviceSync(deviceId, response);
        return response;
    }

    /**
     * 设备添加
     * @param req 设备添加请求体
     * @return 设备id
     */
    @PostMapping("/device/add")
    public DeferredResult<CommonResponse<?>> deviceAdd(@RequestBody DeviceControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(req.getGatewayId(), IdType.GATEWAY).deviceAdd(req.getGatewayId(), req.getDataMap(), response);
        return response;
    }

    /**
     * 设备删除
     * @param deviceId 设备id
     * @return 删除结果
     */
    @DeleteMapping("/device/delete")
    DeferredResult<CommonResponse<?>> deviceDelete(@RequestParam Long deviceId){
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(deviceId, IdType.DEVICE).deviceDelete(deviceId, response);
        return response;
    }

    /**
     * 通道同步
     * @param deviceId 设备id
     * @return 设备id
     */
    @GetMapping("/channel/sync")
    public DeferredResult<CommonResponse<?>> channelSync(@RequestParam Long deviceId){
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(deviceId, IdType.DEVICE).channelSync(deviceId, response);
        return response;
    }

    /**
     * 通道云台控制
     * @param req 设备请求体
     * @return
     */
    @PutMapping("/channel/ptz/control")
    public DeferredResult<CommonResponse<?>> channelPtzControl(@RequestBody DeviceControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(req.getChannelId(), IdType.CHANNEL).channelPtzControl(req.getChannelId(), req.getDataMap(), response);
        return response;
    }

    /**
     * 通道播放
     * @param req 设备请求体
     * @return
     */
    @PostMapping("/channel/video/play")
    public DeferredResult<CommonResponse<?>> channelPlay(@RequestBody DeviceControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(req.getChannelId(), IdType.CHANNEL).channelPlay(req.getChannelId(), req.getDataMap(), response);
        return response;
    }

    /**
     * 通道录像获取
     * @param req 设备请求体
     * @return
     */
    @PostMapping("/channel/video/record")
    public DeferredResult<CommonResponse<?>> channelRecord(@RequestBody DeviceControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(req.getChannelId(), IdType.CHANNEL).channelRecord(req.getChannelId(), req.getDataMap(), response);
        return response;
    }

    /**
     * 设备添加
     * @param req 设备请求体
     * @return
     */
    @PostMapping("/channel/video/playback")
    public DeferredResult<CommonResponse<?>> channelPlayback(@RequestBody DeviceControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getNorthProtocol(req.getChannelId(), IdType.CHANNEL).channelPlayback(req.getChannelId(), req.getDataMap(), response);
        return response;
    }

    /**
     * 自定义扩展事件处理
     * @param req 设备请求体
     * @return
     */
    @PostMapping("/custom/event")
    public DeferredResult<CommonResponse<?>> customEvent(@RequestBody DeviceCustomEventReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        if (Objects.nonNull(req.getGatewayId())){
            protocolService.getNorthProtocol(req.getGatewayId(), IdType.GATEWAY).customEvent(req.getGatewayId(), IdType.GATEWAY, req.getMsgType(), req.getDataMap(), response);
        } else if (Objects.nonNull(req.getDeviceId())) {
            protocolService.getNorthProtocol(req.getDeviceId(), IdType.DEVICE).customEvent(req.getDeviceId(), IdType.DEVICE, req.getMsgType(), req.getDataMap(), response);
        } else if (Objects.nonNull(req.getChannelId())) {
            protocolService.getNorthProtocol(req.getChannelId(), IdType.CHANNEL).customEvent(req.getChannelId(), IdType.CHANNEL, req.getMsgType(), req.getDataMap(), response);
        }else {
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "主键id不能为空");
        }
        return response;
    }



}
