package com.runjian.device.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.vo.feign.DeviceControlReq;
import com.runjian.device.vo.feign.VideoRecordRsp;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.DeviceSyncRsp;
import com.runjian.device.vo.response.VideoPlayRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 解析引擎远程调用
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@FeignClient(value = "parsing-engine")
public interface ParsingEngineApi {


    /**
     * 设备信息同步
     * @param deviceId 设备id
     * @return 设备同步消息体
     */
    @GetMapping("/device/sync")
    CommonResponse<DeviceSyncRsp> deviceSync(@RequestParam Long deviceId);

    /**
     * 设备添加
     * @param req 设备请求体
     * @return 设备id
     */
    @PostMapping("/device/add")
    CommonResponse<Long> deviceAdd(@RequestBody DeviceControlReq req);

    /**
     * 设备删除
     * @param deviceId 设备id
     * @return 删除结果
     */
    @DeleteMapping("/device/delete")
    CommonResponse<Boolean> deviceDelete(@RequestParam Long deviceId);

    /**
     * 通道同步
     * @param deviceId 通道id
     * @return 通道同步结果返回体
     */
    @GetMapping("/channel/sync")
    CommonResponse<ChannelSyncRsp> channelSync(@RequestParam Long deviceId);

    /**
     * 云台控制服务
     * @param req 云台控制请求体
     * @return 是否成功
     */
    @PutMapping("/channel/ptz/control")
    CommonResponse<Boolean> channelPtzControl(@RequestBody DeviceControlReq req);

    /**
     * 通道播放
     * @param req 设备请求体
     * @return
     */
    @PutMapping("/channel/video/play")
    CommonResponse<VideoPlayRsp> channelPlay(@RequestBody DeviceControlReq req);

    /**
     * 通道回放视频获取
     * @param req 设备请求体
     * @return
     */
    @PutMapping("/channel/video/record")
    CommonResponse<VideoRecordRsp> channelRecord(@RequestBody DeviceControlReq req);

    /**
     * 通道回放
     * @param req 设备请求体
     * @return
     */
    @PutMapping("/channel/video/playback")
    CommonResponse<VideoPlayRsp> channelPlayback(@RequestBody DeviceControlReq req);
}
