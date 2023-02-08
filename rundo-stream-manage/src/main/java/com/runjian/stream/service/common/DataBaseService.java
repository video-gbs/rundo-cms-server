package com.runjian.stream.service.common;

import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.entity.StreamInfo;

/**
 * @author Miracle
 * @date 2023/2/3 14:59
 */
public interface DataBaseService {

    DispatchInfo getDispatchInfo(Long dispatchId);

    StreamInfo getStreamInfoByStreamId(String streamId);

}
