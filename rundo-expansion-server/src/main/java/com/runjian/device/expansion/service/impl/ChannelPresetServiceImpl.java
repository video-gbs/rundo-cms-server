package com.runjian.device.expansion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.ChannelPresetLists;
import com.runjian.device.expansion.mapper.ChannelPresetMapper;
import com.runjian.device.expansion.service.IChannelPresetService;
import com.runjian.device.expansion.vo.request.ChannelPresetControlReq;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class ChannelPresetServiceImpl extends ServiceImpl<ChannelPresetMapper, ChannelPresetLists> implements IChannelPresetService {

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
        return null;
    }
}
