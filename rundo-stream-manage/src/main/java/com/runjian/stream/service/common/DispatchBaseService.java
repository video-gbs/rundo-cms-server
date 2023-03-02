package com.runjian.stream.service.common;

import com.runjian.common.utils.CircleArray;

/**
 * @author Miracle
 * @date 2023/2/22 11:06
 */
public interface DispatchBaseService {

    /**
     * 心跳时钟
     */
    CircleArray<Long> HEARTBEAT_ARRAY = new CircleArray<>(600);

    /**
     * 初始化
     */
    void init();

    /**
     * 心跳
     */
    void heartbeat();
}
