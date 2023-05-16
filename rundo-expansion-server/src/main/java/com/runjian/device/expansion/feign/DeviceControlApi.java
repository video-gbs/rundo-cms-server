package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.DeviceControlApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.*;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "device-control",fallbackFactory= DeviceControlApiFallbackFactory.class)
public interface DeviceControlApi {


    /**
     * 控制服务 设备添加
     * @param deviceReq
     * @return
     */
    @PostMapping(value = "/device/north/add",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<DeviceAddResp> deviceAdd(@RequestBody DeviceReq deviceReq);

    /**
     * 控制服务 设备删除
     * @param deviceId
     * @return
     */
    @DeleteMapping("/device/north/soft")
    CommonResponse deleteDeviceSoft(@RequestParam Long deviceId);


    /**
     * 控制服务 设备删除
     * @param deviceId
     * @return
     */
    @DeleteMapping("/device/north/delete")
    CommonResponse deleteDeviceHard(@RequestParam Long deviceId);
    /**
     * 设备注册状态修改
     * @param putDeviceSignSuccessReq
     * @return
     */
    @PutMapping("/device/north/sign/success")
    CommonResponse deviceSignSuccess(@RequestBody PutDeviceSignSuccessReq putDeviceSignSuccessReq);

    /**
     * 控制服务 通道添加状态修改
     * @param putChannelSignSuccessReq
     * @return
     */
    @PutMapping(value = "/channel/north/sign/success",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<Boolean> channelSignSuccess(@RequestBody PutChannelSignSuccessReq putChannelSignSuccessReq);


    /**
     * 控制服务 通道待添加列表
     * @param page
     * @param num
     * @param nameOrOriginId
     * @return
     */
    @GetMapping(value = "/channel/north/page")
    CommonResponse<PageListResp<GetChannelByPageRsp>> getChannelByPage(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10") int num, @RequestParam(required = false) String nameOrOriginId);


    /**
     * 控制服务 通道同步
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/channel/north/sync")
    CommonResponse<ChannelSyncRsp> channelSync(@RequestParam Long deviceId);

    /**
     * 控制服务 通道删除
     * @param channelIds
     * @return
     */
    @DeleteMapping(value = "/channel/north/delete")
    CommonResponse<Boolean> channelDelete(@RequestParam List<Long> channelIds);


    /**
     * 控制服务 通道删除
     * @param channelIds
     * @return
     */
    @DeleteMapping(value = "/channel/north/delete/soft")
    CommonResponse<Boolean> channelDeleteSoft(@RequestParam Long channelIds);

    /**
     * 控制服务 通道删除
     * @param channelIds
     * @return
     */
    @DeleteMapping(value = "/channel/north/delete/hard")
    CommonResponse<Boolean> channelDeleteHard(@RequestParam Long channelIds);
    /**
     * 点播接口
     * @param playFeignReq
     * @return
     */
    @Deprecated
    @PostMapping(value = "/channel/north/play",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> play(@RequestBody PlayFeignReq playFeignReq);

    /**
     * 回放接口
     * @param playBackReq
     * @return
     */
    @PostMapping(value = "/channel/north/playback",produces = MediaType.APPLICATION_JSON_VALUE)
    @Deprecated
    CommonResponse<StreamInfo> playBack(@RequestBody PlayBackFeignReq playBackReq);


    /**
     * 云台控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/channel/north/ptz/control")
    CommonResponse<?> ptzControl(@RequestBody FeignPtzControlReq req);

    /**
     * 获取预置位
     * @param channelId 通道id
     * @return
     */
    @GetMapping("/channel/north/ptz/preset")
    CommonResponse<List<ChannelPresetListsResp>> getPtzPreset(@RequestParam Long channelId);

    /**
     * 3d放大放小控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/channel/north/ptz/3d")
    CommonResponse<?> ptz3d(@RequestBody FeignPtz3dReq req);
}
