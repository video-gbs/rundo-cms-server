package com.runjian.timer.vo.dto;

import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import com.runjian.timer.entity.TemplateDetailInfo;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/9/5 11:31
 */
@Data
public class TimePeriodDto implements ValidatorFunction {

    /**
     * 时间类型 1 星期一 2 星期二 3 星期三 4 星期四 5 星期五 6 星期六 7 星期日
     */
    @NotNull(message = "计划对象不能为空")
    @Range(min = 1, max = 7, message = "计划类型不能为空")
    private Integer dateType;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private  LocalTime endTime;

    /**
     * 是否延续下一天
     */
    private int isNextDay;

    public TemplateDetailInfo toTemplateDetailInfo() {
        TemplateDetailInfo templateDetailInfo = new TemplateDetailInfo();
        templateDetailInfo.setDateType(this.dateType);
        templateDetailInfo.setStartTime(this.startTime);
        templateDetailInfo.setEndTime(this.endTime);
        templateDetailInfo.setIsNextDay(this.isNextDay);
        return templateDetailInfo;
    }

    public static  TimePeriodDto fromTemplateDetailInfo(TemplateDetailInfo templateDetailInfo) {
        TimePeriodDto timePeriodDto = new TimePeriodDto();
        timePeriodDto.setDateType(templateDetailInfo.getDateType());
        timePeriodDto.setStartTime(templateDetailInfo.getStartTime());
        timePeriodDto.setEndTime(templateDetailInfo.getEndTime());
        timePeriodDto.setIsNextDay(templateDetailInfo.getIsNextDay());
        return timePeriodDto;
    }

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (startTime.isAfter(this.endTime)){
            result.setHasErrors(true);
            result.setErrorMsgMap(Map.of("时间有误", "开始时间不能大于结束时间"));
        }
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (o instanceof TimePeriodDto){
            TimePeriodDto timePeriodDto = (TimePeriodDto) o;
            if (!Objects.equals(this.dateType, timePeriodDto.getDateType())){
                return false;
            }
            if (!Objects.equals(this.startTime, timePeriodDto.getStartTime())){
                return false;
            }
            return Objects.equals(this.endTime, timePeriodDto.getEndTime());
        }
        return false;
    }


    @Override
    public int hashCode(){
        int result = this.dateType.hashCode();
        result = 17 * result + this.startTime.hashCode();
        result = 17 * result + this.endTime.hashCode();
        return result;
    }
}
