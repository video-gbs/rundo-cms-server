package com.runjian.device.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.vo.feign.PostDeviceAddReq;
import com.runjian.device.vo.feign.PutPtzControlReq;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.DeviceSyncRsp;
import com.runjian.device.vo.response.VideoPlayRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.time.LocalDateTime;

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
    CommonResponse<DeviceSyncRsp> deviceSync(Long deviceId);

    /**
     * 设备添加
     * @param req 设备添加请求体
     * @return 设备id
     */
    @PostMapping("/device/add")
    CommonResponse<Long> deviceAdd(PostDeviceAddReq req);

    /**
     * 设备删除
     * @param id 设备id
     * @return 删除结果
     */
    @DeleteMapping("/device/delete")
    CommonResponse<Boolean> deviceDelete(Long deviceId);

    /**
     * 通道同步
     * @param deviceId 通道id
     * @return 通道同步结果返回体
     */
    @GetMapping("/channel/sync")
    CommonResponse<ChannelSyncRsp> channelSync(Long deviceId);

    /**
     * 云台控制服务
     * @param req 云台控制请求体
     * @return 是否成功
     */
    @PutMapping("/ptz/control")
    CommonResponse<Boolean> ptzControl(PutPtzControlReq req);

    /**
     * 通道播放
     * @param channelId 通道ID
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是有使用ssrc
     * @param streamMode 流模式
     * @return
     */
    @PutMapping("/video/play")
    CommonResponse<VideoPlayRsp> channelPlay(Long channelId, Boolean enableAudio, Boolean ssrcCheck, String streamMode);

    /**
     * 通道回放
     * @param channelId 通道id
     * @param enableAudio 是否开启音频
     * @param ssrcCheck 是否使用ssrc
     * @param streamMode 流模式
     * @param startTime 录播开始时间
     * @param endTime 录播结束时间
     * @return
     */
    @PutMapping("/video/playback")
    CommonResponse<VideoPlayRsp> channelPlayback(Long channelId, Boolean enableAudio, Boolean ssrcCheck, String streamMode, LocalDateTime startTime, LocalDateTime endTime);
}
