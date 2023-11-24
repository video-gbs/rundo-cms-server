package com.runjian.alarm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.alarm.dao.AlarmMsgInfoMapper;
import com.runjian.alarm.feign.StreamManageApi;
import com.runjian.alarm.service.AlarmMsgNorthService;
import com.runjian.alarm.vo.feign.PostChannelPlayReq;
import com.runjian.alarm.vo.response.GetAlarmMsgRsp;
import com.runjian.alarm.vo.response.GetStreamInfoRsp;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/9/11 17:07
 */
@Service
@RequiredArgsConstructor
public class AlarmMsgNorthServiceImpl implements AlarmMsgNorthService {

    private final AlarmMsgInfoMapper alarmMsgInfoMapper;

    private final StreamManageApi streamManageApi;

    @Override
    public PageInfo<GetAlarmMsgRsp> getAlarmMsgByPage(int page, int num, Long channelId, String alarmDesc, LocalDateTime alarmStartTime, LocalDateTime alarmEndTime, List<Long> channelIds) {
        PageHelper.startPage(page, num);
        if (Objects.isNull(channelIds) || channelIds.isEmpty()){
            channelIds = null;
        }
        return new PageInfo<>(alarmMsgInfoMapper.selectByAlarmDescAndAlarmTime(channelId, alarmDesc, alarmStartTime, alarmEndTime, channelIds));
    }

    @Override
    public GetStreamInfoRsp channelPlay(Long channelId) {
        PostChannelPlayReq playFeignReq = new PostChannelPlayReq(channelId);
        CommonResponse<GetStreamInfoRsp> commonResponse = streamManageApi.play(playFeignReq);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        return commonResponse.getData();
    }

    @Override
    public void deleteAlarmMsg(List<Long> idList) {
        if (idList.isEmpty()){
            return;
        }
        alarmMsgInfoMapper.deleteByIdList(idList);
    }
}
