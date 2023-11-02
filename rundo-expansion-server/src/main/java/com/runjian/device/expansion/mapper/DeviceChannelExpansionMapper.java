package com.runjian.device.expansion.mapper;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionListReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionListReq;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.GetAlarmDeviceChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmSchemeChannelRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 通道信息表
 * @author chenjialing
 */
public interface DeviceChannelExpansionMapper extends BaseMapper<DeviceChannelExpansion> {
    /**
     * 联合分页查询
     * @param page
     * @param deviceChannelExpansionListReq
     * @return
     */
    Page<DeviceChannelExpansionResp> listPage(Page<DeviceChannelExpansion> page, DeviceChannelExpansionListReq deviceChannelExpansionListReq, List<Long> idList);

    /**
     * 告警分页查询
     * @param page
     * @param idList
     * @param channelName
     * @param deviceName
     * @param onlineState
     * @return
     */
    Page<GetAlarmSchemeChannelRsp> listAlarmPage(Page<DeviceChannelExpansion> page, List<Long> idList, String channelName, String deviceName, Integer onlineState, Set<Long> priorityChannelIds);

    /**
     * 告警信息查询
     * @param idList 通道id
     * @return
     */
    List<GetAlarmDeviceChannelRsp> listAlarmList(Set<Long> idList);
}
