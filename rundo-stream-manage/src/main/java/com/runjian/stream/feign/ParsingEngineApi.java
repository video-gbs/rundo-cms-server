package com.runjian.stream.feign;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;

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
    CommonResponse<?> channelStopPlay(String streamId);

    /**
     * 开启录播
     * @param streamId 流id
     * @return
     */
    CommonResponse<Boolean> channelStartRecord(String streamId);

    /**
     * 关闭录像
     * @param streamId 流id
     * @return
     */
    CommonResponse<Boolean> channelStopRecord(String streamId);
}
