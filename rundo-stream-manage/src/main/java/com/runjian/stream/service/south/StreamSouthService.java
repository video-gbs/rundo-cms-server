package com.runjian.stream.service.south;

/**
 * @author Miracle
 * @date 2023/2/7 19:56
 */
public interface StreamSouthService {

    /**
     * 自动关闭
     * @return 是否关闭
     */
    Boolean autoCloseHandle(String streamId);

    /**
     * 接收流播放结果
     */
    void receiveResult(String streamId, Boolean isSuccess);
}
