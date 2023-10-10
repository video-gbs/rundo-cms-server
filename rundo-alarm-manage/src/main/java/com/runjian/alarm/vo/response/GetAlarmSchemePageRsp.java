package com.runjian.alarm.vo.response;

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


    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
