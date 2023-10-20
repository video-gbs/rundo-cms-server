package com.runjian.alarm.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/11 10:46
 */
@Data
public class GetAlarmSchemePageRsp {

    /**
     * 预案id
     */
    private Long id;

    /**
     * 预案名称
     */
    private String schemeName;

    /**
     * 事件名称数组
     */
    private List<String> eventNameList;

    /**
     * 是否禁用
     */
    private Integer disabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
