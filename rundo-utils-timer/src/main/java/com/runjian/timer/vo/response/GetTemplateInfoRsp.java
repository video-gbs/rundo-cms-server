package com.runjian.timer.vo.response;

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
     * 时间段
     */
    private List<TimePeriodDto> timePeriodDtoList;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
