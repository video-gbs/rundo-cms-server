package com.runjian.stream.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.stream.vo.StreamManageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/6 15:27
 */
@FeignClient(value = "parsing-engine")
public interface ParsingEngineApi {

    /**
     * 停止播放
     * @param req 统一请求体
     * @return
     */
    @PutMapping("/stream-manage/play/stop")
    CommonResponse<Boolean> channelStopPlay(@RequestBody StreamManageDto req);

    /**
     * 开启录播
     * @param req 统一请求体
     * @return
     */
    @PutMapping("/stream-manage/record/start")
    CommonResponse<Boolean> channelStartRecord(@RequestBody StreamManageDto req);

    /**
     * 关闭录像
     * @param req 统一请求体
     * @return
     */
    @PutMapping("/stream-manage/record/stop")
    CommonResponse<Boolean> channelStopRecord(@RequestBody StreamManageDto req);

    /**
     * 检测录像状态
     * @param dispatchId 调度服务id
     * @param streamIds 流id数组
     * @return
     */
    @GetMapping("/stream-manage/check/record")
    CommonResponse<List<String>> checkStreamRecordStatus(@RequestParam Long dispatchId, @RequestParam List<String> streamIds);

    /**
     * 检测流状态
     * @param dispatchId 调度服务id
     * @param streamIds 流id数组
     * @return
     */
    @GetMapping("/stream-manage/check/stream")
    CommonResponse<List<String>> checkStreamStreamStatus(@RequestParam Long dispatchId, @RequestParam List<String> streamIds);

    /**
     * 删除所有的流
     * @param dispatchIds 调度服务id数组
     */
    @DeleteMapping("/stream-manage/stream/stop/all")
    CommonResponse<?> stopAllStream(Set<Long> dispatchIds);
}
