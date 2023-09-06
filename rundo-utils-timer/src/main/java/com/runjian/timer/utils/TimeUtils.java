package com.runjian.timer.utils;

import com.runjian.timer.vo.dto.TimePeriodDto;

import java.util.*;

/**
 * @author Miracle
 * @date 2023/9/5 10:48
 */
public class TimeUtils {

    /**
     * 校验时间范围是否准确
     * @param timePeriodDtoList
     * @return
     */
    public static boolean validTimeRange(Set<TimePeriodDto> timePeriodDtoList){
        // 判断数据是否为空
        if (timePeriodDtoList.isEmpty()){
            return false;
        }
        HashMap<Integer, List<TimePeriodDto>> typePlanTimeMap = new HashMap<>();
        // 循环创建一周的录像时间计划
        for(int i = 1; i <= 7; i++){
            typePlanTimeMap.put(i, new ArrayList<>());
        }
        // 遍历将录像计划时间分类进map中
        timePeriodDtoList.forEach(timePeriodDto -> typePlanTimeMap.get(timePeriodDto.getDateType()).add(timePeriodDto));

        // 遍历循环一周的录像计划进行检测
        for (List<TimePeriodDto> typePlanTimeList : typePlanTimeMap.values()){

            if (typePlanTimeList.size() < 2){
                continue;
            }
            // 将List转换为 k-开始时间 v-结束时间
            Map<Integer, Integer> dateMap = new HashMap<>(typePlanTimeList.size());
            for (TimePeriodDto recordPlanTime : typePlanTimeList){
                dateMap.put(recordPlanTime.getStartTime().toSecondOfDay(), recordPlanTime.getEndTime().toSecondOfDay());
            }
            // 判断是否有重复的开始时间
            if (typePlanTimeList.size() > dateMap.size()){
                return true;
            }

            ArrayList<Integer> startTimeList = new ArrayList<>(dateMap.keySet());
            // 对开始时间进行排序
            Collections.sort(startTimeList);
            // 判断结束时间的范围对不对
            for (int i = 0; i < startTimeList.size() - 1; i++){
                int preStartTime = startTimeList.get(i);
                int sufStartTime = startTimeList.get(i + 1);
                int endTime = dateMap.get(startTimeList.get(i));
                // 判断结束时间是否小于等于开始时间，判断结束时间是否大于后面的开始时间
                if (endTime <= preStartTime || endTime >= sufStartTime){
                    return true;
                }
            }
        }
        return false;
    }
}
