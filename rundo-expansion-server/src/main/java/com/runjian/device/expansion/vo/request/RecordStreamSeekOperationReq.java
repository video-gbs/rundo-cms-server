package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/7 20:46
 */
@Data
public class RecordStreamSeekOperationReq extends RecordStreamOperationReq{

    /**
     * 当前时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("当前时间")
    private LocalDateTime currentTime;

    /**
     * 目标时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("目标时间")
    private LocalDateTime targetTime;


}
