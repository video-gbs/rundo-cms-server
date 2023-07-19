package com.runjian.device.expansion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.AuthRbacServerApi;
import com.runjian.device.expansion.mapper.DeviceChannelExpansionMapper;
import com.runjian.device.expansion.mapper.DeviceExpansionMapper;
import com.runjian.device.expansion.service.IBaseDeviceAndChannelService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.feign.request.GetCatalogueResourceRsp;
import com.runjian.device.expansion.vo.feign.request.PostBatchResourceReq;
import com.runjian.device.expansion.vo.feign.request.PutResourceFsMoveReq;
import com.runjian.device.expansion.vo.feign.request.ResourceFsMoveKvReq;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResourceRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class BaseDeviceAndChannelServiceImpl implements IBaseDeviceAndChannelService {

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Autowired
    DeviceChannelExpansionMapper deviceChannelExpansionMapper;

    @Autowired
    DeviceExpansionMapper deviceExpansionMapper;

    @Autowired
    AuthRbacServerApi authrbacServerApi;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeDevice(Long id) {
        deviceExpansionMapper.deleteById(id);
        //删除对应的通道
        LambdaQueryWrapper<DeviceChannelExpansion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeviceChannelExpansion::getDeviceExpansionId,id);
        deviceChannelExpansionMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public void removeDeviceSoft(Long id) {

        DeviceExpansion deviceExpansion = new DeviceExpansion();
        deviceExpansion.setId(id);
        deviceExpansion.setDeleted(1);

        deviceExpansionMapper.updateById(deviceExpansion);
        //删除对应的通道
        LambdaQueryWrapper<DeviceChannelExpansion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeviceChannelExpansion::getDeviceExpansionId,id);
        DeviceChannelExpansion channelExpansion = new DeviceChannelExpansion();
        channelExpansion.setDeleted(1);
        deviceChannelExpansionMapper.update(channelExpansion,lambdaQueryWrapper);
    }

    @Override
    public VideoAreaResourceRsp resourceIdList(Long videoAreaId, Boolean includeEquipment) {
        //获取安防通道资源
        videoAreaId = 39L;
        CommonResponse<List<GetCatalogueResourceRsp>> catalogueResourceRsp = authrbacServerApi.getCatalogueResourceRsp(videoAreaId, includeEquipment);
        if(catalogueResourceRsp.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){

            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, catalogueResourceRsp.getMsg());
        }
        List<GetCatalogueResourceRsp> dataList = catalogueResourceRsp.getData();
        ArrayList<Long> longs = new ArrayList<>();
        //获取全部的资源树id
        for (GetCatalogueResourceRsp one : dataList){
            longs.add(Long.parseLong(one.getResourceValue()));
        }
        if(ObjectUtils.isEmpty(longs)){
            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, "数据数据不存在");
        }
        VideoAreaResourceRsp videoAreaResourceRsp = new VideoAreaResourceRsp();
        videoAreaResourceRsp.setChannelData(longs);
        videoAreaResourceRsp.setDataList(dataList);
        return videoAreaResourceRsp;
    }

    @Override
    public void commonResourceBind(Long videoAreaId, Long resourceId, String resourceName) {
        PostBatchResourceReq postBatchResourceReq = new PostBatchResourceReq();
        postBatchResourceReq.setResourcePid(videoAreaId);
        postBatchResourceReq.setResourceType(2);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put(resourceId.toString(), resourceName);
        postBatchResourceReq.setResourceMap(stringStringHashMap);
        CommonResponse<?> commonResponse = authrbacServerApi.batchAddResource(postBatchResourceReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器资源绑定失败",videoAreaId, commonResponse);
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
    }

    @Override
    public void commonResourceUpdate(Long resourceId, Long pid) {
        PutResourceFsMoveReq putResourceFsMoveReq = new PutResourceFsMoveReq();
        putResourceFsMoveReq.setId(resourceId);
        putResourceFsMoveReq.setSectionPid(pid);
        CommonResponse<?> commonResponse = authrbacServerApi.fsMove(putResourceFsMoveReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器资源移动失败",putResourceFsMoveReq, commonResponse);
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
    }

    @Override
    public void moveResourceByValue(String resourceKey, String resourceValue, String pResourceValue) {
        ResourceFsMoveKvReq resourceFsMoveKvReq = new ResourceFsMoveKvReq();
        resourceFsMoveKvReq.setResourceKey(resourceKey);
        resourceFsMoveKvReq.setResourceValue(resourceValue);
        resourceFsMoveKvReq.setPResourceValue(pResourceValue);
        CommonResponse<?> commonResponse = authrbacServerApi.moveResourceValue(resourceFsMoveKvReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--资源移动失败",resourceFsMoveKvReq, commonResponse);
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
    }
}
