package com.runjian.timer.vo.request;

import com.runjian.common.constant.CommonEnum;
import com.runjian.timer.entity.TemplateDetailInfo;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Miracle
 * @date 2022/4/20 9:47
 */
@Data
public class PostTemplateDetailReq {

    /**
     * 计划类型 1-周一 2-周二 3-周三 4-周四 5-周五 6-周六 7-周日
     */
    @NotNull(message = "计划对象不能为空")
    @Range(min = 1, max = 7, message = "计划类型不能为空")
    private Integer type;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalTime endTime;

    /**
     * 是否24点跨日
     */
    @NotNull
    @Range(min = 0, max = 1, message = "是否24点跨日不能为空")
    private Integer isNextDay;

    /**
     * 转化为数据库对象
     * @return
     */
    public TemplateDetailInfo toTemplateDetailInfo(){
        TemplateDetailInfo templateDetailInfo = new TemplateDetailInfo();
        BeanUtils.copyProperties(this, templateDetailInfo);
        LocalDateTime nowTime = LocalDateTime.now();
        templateDetailInfo.setCreateTime(nowTime);
        templateDetailInfo.setUpdateTime(nowTime);
        // 判断跨日设置是否合理
        if (getIsNextDay().equals(CommonEnum.ENABLE.getCode()) && (getEndTime().getHour() != 23 || getEndTime().getMinute() != 59)){
            setIsNextDay(CommonEnum.DISABLE.getCode());
        }
        return templateDetailInfo;
    }
}
