package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.vo.feign.StreamPlayReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

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
    @PostMapping("/stream/play")
    CommonResponse<?> applyPlay(StreamPlayReq streamPlayReq);

}
