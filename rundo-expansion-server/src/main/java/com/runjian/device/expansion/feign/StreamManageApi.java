package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.StreamManageApiFallbackFactory;
import com.runjian.device.expansion.vo.request.RecordStreamOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSeekOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSpeedOperationReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "stream-manage",fallbackFactory= StreamManageApiFallbackFactory.class)
public interface StreamManageApi {

    /**
     * 开启录像
     * @param req
     * @return
     */
    @PutMapping("/record/pause")
    public CommonResponse<Boolean> recordPause(@RequestBody RecordStreamOperationReq req);


    /**
     * 停止录像
     * @param req
     * @return
     */
    @PutMapping("/record/resume")
    public CommonResponse<Boolean> recordResume(@RequestBody RecordStreamOperationReq req);


    /**
     * 调整录播的播放速度
     * @param req
     * @return
     */
    @PutMapping("/record/speed")
    public CommonResponse<?> recordSpeed(@RequestBody RecordStreamSpeedOperationReq req);


    @PutMapping("/record/seek")
    public CommonResponse<?> recordSeek(@RequestBody RecordStreamSeekOperationReq req);
}
