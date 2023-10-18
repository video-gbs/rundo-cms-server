package com.runjian.timer.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.timer.vo.dto.TimePeriodDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取模板信息响应对象
 * @author Miracle
 * @date 2023/9/6 14:51
 */
@Data
public class GetTemplateInfoRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 时间类型
     */
    private List<String> dateTypeStrList;

    /**
     * 时间段
     */
    private List<TimePeriodDto> timePeriodDtoList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
