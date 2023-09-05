package com.runjian.timer.vo.request;


import com.runjian.common.constant.CommonEnum;
import com.runjian.common.validator.constraints.annotation.NotSpecialChar;
import com.runjian.timer.entity.TemplateDetailInfo;
import com.runjian.timer.vo.dto.TimePeriodDto;
import lombok.Data;
import org.yaml.snakeyaml.util.ArrayStack;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2022/4/20 9:40
 */
@Data
public class PostTemplateReq  {

    /**
     * 计划名称
     */
    @NotSpecialChar(except = {"-"},message = "计划名称不能包含特殊字符")
    @NotBlank(message = "计划名称不能为空")
    @Size(min = 1, max = 32, message = "计划名称过长")
    private String planName;

    /**
     * 录像时间
     */
    @Size(max = 100, message = "录像时间段过多")
    private List<TimePeriodDto> timePeriodList;


    /**
     * 获取转化后的录像对象数组
     * @return
     */
    public List<TemplateDetailInfo> getTemplateDetailInfoList(){
        Map<Integer, List<TimePeriodDto>> timePeriodMap = timePeriodList.stream().collect(Collectors.groupingBy(TimePeriodDto::getDateType, Collectors.toList()));
        Map<String, TimePeriodDto> nextDayMap = new HashMap<>(7);
        LocalTime startTime = LocalTime.of(0,0, 0);
        LocalTime endTime = LocalTime.of(23,59,59);
        for (int i = 1; i <= 7; i++){
            for (TimePeriodDto timePeriodDto : timePeriodMap.get(i)){
                if (timePeriodDto.getStartTime().equals(startTime)){
                    if (i == 1){
                        nextDayMap.put("start:1", null);
                    } else if (nextDayMap.containsKey("end:" + (i - 1))){
                        nextDayMap.get("end:" + (i - 1)).setIsNextDay(CommonEnum.ENABLE.getCode());
                    }
                }
                if (timePeriodDto.getEndTime().equals(endTime)){
                    if (i == 7 && nextDayMap.containsKey("start:1")){
                        timePeriodDto.setIsNextDay(CommonEnum.ENABLE.getCode());
                    } else {
                        nextDayMap.put("end:" + i, timePeriodDto);
                    }
                }
            }
        }
        return null;
    }


}
