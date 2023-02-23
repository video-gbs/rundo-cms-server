package com.runjian.stream.service.common;

import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.entity.StreamInfo;

/**
 * @author Miracle
 * @date 2023/2/3 14:59
 */
public interface DataBaseService {

    /**
     * 获取调度服务信息
     * @param dispatchId
     * @return
     */
    DispatchInfo getDispatchInfo(Long dispatchId);

    /**
     * 通过流id获取流信息
     * @param streamId
     * @return
     */
    StreamInfo getStreamInfoByStreamId(String streamId);

}
