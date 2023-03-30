package com.runjian.stream.vo.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime currentTime;

    /**
     * 目标时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime targetTime;
}
