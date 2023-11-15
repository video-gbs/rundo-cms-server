package com.runjian.timer.utils;

import com.runjian.common.constant.CommonEnum;
import com.runjian.timer.vo.dto.TimePeriodDto;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/9/5 10:48
 */
@Slf4j
public class TimeUtils {

    /**
     * 校验时间范围是否准确
     * @param timePeriodDtoList
     * @return
     */
    public static boolean validTimeRange(Set<TimePeriodDto> timePeriodDtoList){
        // 判断数据是否为空
        if (Objects.isNull(timePeriodDtoList) || timePeriodDtoList.isEmpty()){
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

    public static Set<TimePeriodDto> setNextDay(Set<TimePeriodDto> timePeriodList){
        LocalTime startTime = LocalTime.of(0,0, 0);
        LocalTime endTime = LocalTime.of(23,59,59);
        List<TimePeriodDto> timePeriodStartAndEndList = timePeriodList.stream().filter(timePeriodDto -> timePeriodDto.getStartTime().equals(startTime) || timePeriodDto.getEndTime().equals(endTime)).collect(Collectors.toList());
        Map<Integer, TimePeriodDto> nextDayMap = new HashMap<>(7);
        for (TimePeriodDto timePeriodDto : timePeriodStartAndEndList){
            if (timePeriodDto.getStartTime().equals(startTime)){
                if (timePeriodDto.getDateType() == 1){
                    nextDayMap.put(timePeriodDto.getDateType(), null);
                } else if (nextDayMap.containsKey(timePeriodDto.getDateType() - 1)){
                    if (Objects.nonNull(nextDayMap.get(timePeriodDto.getDateType() - 1))){
                        nextDayMap.get(timePeriodDto.getDateType() - 1).setIsNextDay(CommonEnum.ENABLE.getCode());
                    }
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
        timePeriodList.addAll(nextDayMap.values().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return timePeriodList;
    }
}
