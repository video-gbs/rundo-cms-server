package com.runjian.device.expansion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.ChannelPresetLists;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.mapper.ChannelPresetMapper;
import com.runjian.device.expansion.service.IChannelPresetService;
import com.runjian.device.expansion.vo.request.ChannelPresetControlReq;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class ChannelPresetServiceImpl extends ServiceImpl<ChannelPresetMapper, ChannelPresetLists> implements IChannelPresetService {

    @Autowired
    ChannelPresetMapper channelPresetMapper;

    @Override
    public CommonResponse<ChannelPresetListsResp> presetSelect(Long channelExpansionId) {
        //考虑部分数据修改
        return null;
    }

    @Override
    public CommonResponse<Boolean> presetEdit(Long channelExpansionId) {
        return null;
    }

    @Override
    public CommonResponse<Boolean> presetDelete(Long channelExpansionId) {
        return null;
    }

    @Override
    public CommonResponse<Boolean> presetInvoke(ChannelPresetControlReq channelPresetControlReq) {
        LambdaQueryWrapper<ChannelPresetLists> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelPresetLists::getChannelExpansionId,channelPresetControlReq.getChannelExpansionId());
        Long one = channelPresetMapper.selectCount(queryWrapper);
        if(one <=0 ){
            //预置位不存在
            throw new BusinessException(BusinessErrorEnums.RESULT_DATA_NONE);
        }

        return null;
    }
}
