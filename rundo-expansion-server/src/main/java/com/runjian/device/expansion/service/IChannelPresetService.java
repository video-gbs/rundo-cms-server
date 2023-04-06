package com.runjian.device.expansion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.ChannelPresetLists;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.ChannelPresetControlReq;
import com.runjian.device.expansion.vo.request.PlayBackReq;
import com.runjian.device.expansion.vo.request.PlayReq;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenjialing
 */
public interface IChannelPresetService extends IService<ChannelPresetLists> {

    /**
     * 预置位查询
     * @param channelExpansionId
     * @return
     */
    CommonResponse<ChannelPresetListsResp> presetSelect(Long channelExpansionId);


    /**
     * 预置位编辑
     * @param channelExpansionId
     * @return
     */
    CommonResponse<Boolean> presetEdit(Long channelExpansionId);


    /**
     * 预置位删除
     * @param channelExpansionId
     * @return
     */
    CommonResponse<Boolean> presetDelete(Long channelExpansionId);

    /**
     * 预置位执行
     * @param channelPresetControlReq
     * @return
     */
    CommonResponse<Boolean> presetInvoke(ChannelPresetControlReq channelPresetControlReq);
}
