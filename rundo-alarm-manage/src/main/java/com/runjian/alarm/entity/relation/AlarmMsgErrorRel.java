package com.runjian.alarm.entity.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/15 15:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmMsgErrorRel {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 告警信息id
     */
    private Long alarmMsgId;

    /**
     * 告警文件类型
     */
    private Integer alarmFileType;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
