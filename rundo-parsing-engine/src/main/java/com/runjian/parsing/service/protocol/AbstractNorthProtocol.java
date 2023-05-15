package com.runjian.parsing.service.protocol;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.IdType;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MsgType;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.protocol.NorthProtocol;
import com.runjian.parsing.vo.dto.GatewayChannelIdData;
import com.runjian.parsing.vo.dto.GatewayConvertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认协议
 * @author Miracle
 * @date 2023/1/17 14:14
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractNorthProtocol implements NorthProtocol {

    protected final GatewayTaskService gatewayTaskService;

    protected final DataBaseService dataBaseService;

    protected final DeviceMapper deviceMapper;

    protected final ChannelMapper channelMapper;

    @Override
    public void msgDistribute(MsgType msgType, Long mainId, IdType idType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        switch (msgType){
            case DEVICE_DELETE:
                deviceDelete(mainId, response);
                return;
            case CHANNEL_DELETE:
                channelDelete(mainId, response);
                return;
            default:
                customEvent(mainId, idType, msgType.getMsg(), dataMap, response);
        }
    }

    @Override
    public void customEvent(Long mainId, IdType idType, String msgType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        GatewayConvertDto gatewayConvertDto = new GatewayConvertDto();
        gatewayConvertDto.setDataMap(dataMap);
        Long deviceId = null;
        Long channelId = null;
        switch (idType){
            case CHANNEL:
                ChannelInfo channelInfo = dataBaseService.getChannelInfo(mainId);
                gatewayConvertDto.setChannelId(channelInfo.getOriginId());
                channelId = mainId;
                mainId = channelInfo.getDeviceId();
            case DEVICE:
                DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(mainId);
                gatewayConvertDto.setDeviceId(deviceInfo.getOriginId());
                deviceId = mainId;
                mainId = deviceInfo.getGatewayId();
            case GATEWAY:
                gatewayTaskService.sendMsgToGateway(mainId, deviceId, channelId, msgType, gatewayConvertDto, response);
                break;
        }
    }

    public void deviceDelete(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(deviceId);
        if (deviceInfoOp.isEmpty()){
            response.setResult(CommonResponse.success(true));
        } else {
            customEvent(deviceId, IdType.DEVICE, MsgType.DEVICE_DELETE.getMsg(), null, response);
        }
    }

    public void channelDelete(Long channelId, DeferredResult<CommonResponse<?>> response){
        Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(channelId);
        if (channelInfoOp.isEmpty()){
            response.setResult(CommonResponse.success(true));
        }else {
            customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_DELETE.getMsg(), null, response);
        }
    }


//    public void channelDeleteForced(Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response){
//        List<Long> channelIdList = (List<Long>)dataMap.get(StandardName.CHANNEL_ID_LIST);
//        List<ChannelInfo> channelInfoList = channelMapper.selectByIds(channelIdList);
//        List<GatewayChannelIdData> gatewayChannelIdDataList = channelMapper.selectIdAndGatewayIdByChannelIds(channelIdList);
//        List<Long> collect = channelInfoList.stream().map(ChannelInfo::getId).collect(Collectors.toList());
//        if (channelInfoList.size() != channelIdList.size()){
//            channelIdList.removeAll(collect);
//            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("缺失的数据%s", channelIdList));
//        }
//
//        Map<Long, List<Long>> gatewayChannelMap = new HashMap<>();
//        for (GatewayChannelIdData gatewayChannelIdData : gatewayChannelIdDataList){
//            List<Long> delChannelIds = gatewayChannelMap.get(gatewayChannelIdData.getGatewayId());
//            if (Objects.isNull(delChannelIds)){
//                delChannelIds = new ArrayList<>();
//            }
//            delChannelIds.add(gatewayChannelIdData.getChannelId());
//        }
//
//        for (Map.Entry<Long, List<Long>> entry : gatewayChannelMap.entrySet()){
//            customEvent(entry.getKey(), IdType.GATEWAY, MsgType.CHANNEL_FORCED_DELETE.getMsg(), Map.of(StandardName.CHANNEL_ID_LIST, entry.getValue()), response);
//        }
//        channelMapper.batchDelete(collect);
//    }

}
