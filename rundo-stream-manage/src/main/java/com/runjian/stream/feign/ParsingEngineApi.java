package com.runjian.stream.feign;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Miracle
 * @date 2023/2/6 15:27
 */
@FeignClient(value = "parsing-engine")
public interface ParsingEngineApi {

    /**
     * 停止播放
     * @param streamId 流id
     * @return
     */
    @GetMapping("/channel/stream/stop")
    CommonResponse<?> channelStopPlay(String streamId);

    /**
     * 开启录播
     * @param streamId 流id
     * @return
     */
    @GetMapping("/channel/stream/record/start")
    CommonResponse<Boolean> channelStartRecord(String streamId);

    /**
     * 关闭录像
     * @param streamId 流id
     * @return
     */
    @GetMapping("/channel/stream/record/stop")
    CommonResponse<Boolean> channelStopRecord(String streamId);
}
