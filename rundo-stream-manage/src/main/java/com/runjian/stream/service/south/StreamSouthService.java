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
    Boolean streamCloseHandle(String streamId, Boolean isError);

    /**
     * 接收流播放结果
     */
    void receiveResult(String streamId, Boolean isSuccess);

    /**
     * 接收录像文件结果
     */
    //void receiveRecordResult(String streamId, LocalDateTime startTime, LocalDateTime endTime, );
}
