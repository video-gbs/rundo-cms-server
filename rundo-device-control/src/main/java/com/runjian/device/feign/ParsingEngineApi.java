package com.runjian.device.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.DeviceSyncRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 解析引擎远程调用
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@FeignClient(value = "parsing-engine")
public interface ParsingEngineApi {


    /**
     * 设备信息同步
     * @param id 设备id
     * @return 设备同步消息体
     */
    @GetMapping("/device/sync")
    CommonResponse<DeviceSyncRsp> deviceSync(Long id);

    /**
     * 设备添加
     * @param deviceId 设备原始id
     * @param gatewayId 网关id
     * @return 设备id
     */
    @PostMapping("/device/sign-in")
    CommonResponse<Long> deviceAdd(String deviceId, Long gatewayId);

    /**
     * 设备删除
     * @param id 设备id
     * @return 删除结果
     */
    @DeleteMapping("/device/delete")
    CommonResponse<Boolean> deviceDelete(Long id);

    /**
     * 通道同步
     * @param deviceId 通道id
     * @return 通道同步结果返回体
     */
    @PostMapping("/channel/sync")
    CommonResponse<ChannelSyncRsp> channelSync(Long deviceId);
}
