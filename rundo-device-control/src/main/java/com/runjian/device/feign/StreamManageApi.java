package com.runjian.device.feign;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "stream-manage")
public interface StreamManageApi {


    /**
     * 申请播放
     * @param channelId 通道id
     * @param gatewayId 网关id
     * @param isPlayback 是否是回播
     * @return
     */
    CommonResponse<String> applyPlay(Long channelId, Long gatewayId, Boolean isPlayback);

}
