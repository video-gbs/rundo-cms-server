package com.runjian.stream.vo.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/3/30 9:44
 */
@Data
public class PutRecordSeekReq extends PutStreamOperationReq {

    /**
     * 当前时间
     */
    private LocalDateTime currentTime;

    /**
     * 目标时间
     */
    private LocalDateTime targetTime;
}
