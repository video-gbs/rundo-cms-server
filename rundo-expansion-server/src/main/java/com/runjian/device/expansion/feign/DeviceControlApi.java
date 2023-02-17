package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.DeviceControlApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.DeviceReq;
import com.runjian.device.expansion.vo.feign.request.PlayBackFeignReq;
import com.runjian.device.expansion.vo.feign.request.PlayFeignReq;
import com.runjian.device.expansion.vo.feign.request.PutChannelSignSuccessReq;
import com.runjian.device.expansion.vo.feign.response.ChannelSyncRsp;
import com.runjian.device.expansion.vo.feign.response.GetChannelByPageRsp;
import com.runjian.device.expansion.vo.feign.response.PageListResp;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
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
    CommonResponse<Long> deviceAdd(@RequestBody DeviceReq deviceReq);

    /**
     * 控制服务 设备删除
     * @param deviceId
     * @return
     */
    @DeleteMapping("/device/north/delete")
    CommonResponse deleteDevice(@RequestParam Long deviceId);

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
     * 点播接口
     * @param playFeignReq
     * @return
     */
    @PostMapping(value = "/channel/north/play",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> play(@RequestBody PlayFeignReq playFeignReq);

    /**
     * 回放接口
     * @param playBackReq
     * @return
     */
    @PostMapping(value = "/channel/north/playback",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> playBack(@RequestBody PlayBackFeignReq playBackReq);
}
