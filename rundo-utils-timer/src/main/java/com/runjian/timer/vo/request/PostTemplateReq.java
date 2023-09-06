package com.runjian.timer.vo.request;


import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import com.runjian.common.validator.constraints.annotation.NotSpecialChar;
import com.runjian.timer.entity.TemplateDetailInfo;
import com.runjian.timer.utils.TimeUtils;
import com.runjian.timer.vo.dto.TimePeriodDto;
import lombok.Data;
import org.yaml.snakeyaml.util.ArrayStack;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2022/4/20 9:40
 */
@Data
public class PostTemplateReq implements ValidatorFunction {

    /**
     * 计划名称
     */
    @NotSpecialChar(except = {"-"},message = "计划名称不能包含特殊字符")
    @NotBlank(message = "计划名称不能为空")
    @Size(min = 1, max = 32, message = "计划名称过长")
    private String templateName;

    /**
     * 录像时间
     */
    @Size(max = 100, message = "录像时间段过多")
    private Set<TimePeriodDto> timePeriodList;

    /**
     * 获取转化后的录像对象数组
     * @return
     */
    public List<TemplateDetailInfo> getTemplateDetailInfoList(){
        LocalTime startTime = LocalTime.of(0,0, 0);
        LocalTime endTime = LocalTime.of(23,59,59);
        List<TimePeriodDto> timePeriodStartAndEndList = this.timePeriodList.stream().filter(timePeriodDto -> timePeriodDto.getStartTime().equals(startTime) || timePeriodDto.getEndTime().equals(endTime)).collect(Collectors.toList());
        Map<Integer, TimePeriodDto> nextDayMap = new HashMap<>(7);
        for (TimePeriodDto timePeriodDto : timePeriodStartAndEndList){
            if (timePeriodDto.getStartTime().equals(startTime)){
                if (timePeriodDto.getDateType() == 1){
                    nextDayMap.put(timePeriodDto.getDateType(), null);
                } else if (nextDayMap.containsKey(timePeriodDto.getDateType() - 1)){
                    nextDayMap.get(timePeriodDto.getDateType() - 1).setIsNextDay(CommonEnum.ENABLE.getCode());
                }
            }
            if (timePeriodDto.getEndTime().equals(endTime)){
                if (timePeriodDto.getDateType() == 7 && nextDayMap.containsKey(1)){
                    timePeriodDto.setIsNextDay(CommonEnum.ENABLE.getCode());
                } else {
                    nextDayMap.put(timePeriodDto.getDateType(), timePeriodDto);
                }
            }
        }
        // 替换数据
        this.timePeriodList.addAll(nextDayMap.values().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return this.timePeriodList.stream().map(TimePeriodDto::toTemplateDetailInfo).collect(Collectors.toList());
    }

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (TimeUtils.validTimeRange(this.timePeriodList)){
            throw new BusinessException(BusinessErrorEnums.VALID_PARAMETER_ERROR, "传入的时间范围错误");
        }
    }
}
