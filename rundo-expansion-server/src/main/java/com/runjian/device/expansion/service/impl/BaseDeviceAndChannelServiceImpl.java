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
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResourceRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
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

    @Value("${resourceKeys.deviceKey:safety_device}")
    String resourceDeviceKey;

    @Value("${resourceKeys.channelKey:safety_channel}")
    String resourceChannelKey;


    @Override
    public synchronized void removeDeviceSoft(Long id) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        //获取通道数据
        LambdaQueryWrapper<DeviceChannelExpansion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeviceChannelExpansion::getDeviceExpansionId,id);
        lambdaQueryWrapper.eq(DeviceChannelExpansion::getDeleted,0);
        List<DeviceChannelExpansion> deviceChannelExpansions = deviceChannelExpansionMapper.selectList(lambdaQueryWrapper);
        try{
            DeviceExpansion deviceExpansion = new DeviceExpansion();
            deviceExpansion.setId(id);
            deviceExpansion.setDeleted(1);
            deviceExpansionMapper.updateById(deviceExpansion);
            //删除对应的通道
            LambdaQueryWrapper<DeviceChannelExpansion> lambdaQueryWrapperUpdate = new LambdaQueryWrapper<>();
            lambdaQueryWrapperUpdate.eq(DeviceChannelExpansion::getDeviceExpansionId,id);
            DeviceChannelExpansion channelExpansion = new DeviceChannelExpansion();
            channelExpansion.setDeleted(1);
            deviceChannelExpansionMapper.update(channelExpansion,lambdaQueryWrapper);

            dataSourceTransactionManager.commit(transactionStatus);

        }catch (Exception e){
            log.error(LogTemplate.PROCESS_LOG_TEMPLATE, "设备与通道删除", "删除异常",e);
            dataSourceTransactionManager.rollback(transactionStatus);
        }


        commonDeleteByResourceValue(resourceDeviceKey,String.valueOf(id));
        if(!ObjectUtils.isEmpty(deviceChannelExpansions)){
            deviceChannelExpansions.forEach(deviceChannelExpansion -> {
                //删除通道资源
                commonDeleteByResourceValue(resourceChannelKey,String.valueOf(deviceChannelExpansion.getId()));
            });
        }
    }

    @Override
    public VideoAreaResourceRsp resourceIdList(Long videoAreaId, Boolean includeEquipment) {
        //获取安防通道资源
        CommonResponse<List<GetCatalogueResourceRsp>> catalogueResourceRsp = authrbacServerApi.getCatalogueResourceRsp(videoAreaId,includeEquipment);
        if(catalogueResourceRsp.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){

            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, catalogueResourceRsp.getMsg());
        }
        List<GetCatalogueResourceRsp> dataList = catalogueResourceRsp.getData();
        ArrayList<Long> longs = new ArrayList<>();
        //获取全部的资源树id
        for (GetCatalogueResourceRsp one : dataList){
            longs.add(Long.parseLong(one.getResourceValue()));
        }
        VideoAreaResourceRsp videoAreaResourceRsp = new VideoAreaResourceRsp();
        videoAreaResourceRsp.setChannelData(longs);
        videoAreaResourceRsp.setDataList(dataList);
        return videoAreaResourceRsp;
    }

    @Override
    public void commonResourceBind(String resourceKey,String pResourceValue, Long resourceId, String resourceName) {
        PostBatchResourceKvReq postBatchResourceKvReq = new PostBatchResourceKvReq();
        postBatchResourceKvReq.setResourceType(2);
        postBatchResourceKvReq.setResourceKey(resourceKey);
        postBatchResourceKvReq.setParentResourceValue(pResourceValue);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put(resourceId.toString(), resourceName);
        postBatchResourceKvReq.setResourceMap(stringStringHashMap);

        CommonResponse<?> commonResponse = authrbacServerApi.batchAddResourceKv(postBatchResourceKvReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器资源绑定失败",pResourceValue, commonResponse);
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
    }

    @Override
    public void commonResourceUpdate(String resourceKey, String resourceValue, String resourceName) {

        PutResourceReq putResourceReq = new PutResourceReq();
        putResourceReq.setResourceKey(resourceKey);
        putResourceReq.setResourceName(resourceName);
        putResourceReq.setResourceValue(resourceValue);
        CommonResponse<?> commonResponse = authrbacServerApi.updateResourceKv(putResourceReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--资源修改失败",putResourceReq, commonResponse);
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
    }

    @Override
    public void moveResourceByValue(String resourceKey, String resourceValue, String pResourceValue) {
        ResourceFsMoveKvReq resourceFsMoveKvReq = new ResourceFsMoveKvReq();
        resourceFsMoveKvReq.setResourceKey(resourceKey);
        resourceFsMoveKvReq.setResourceValue(resourceValue);
        resourceFsMoveKvReq.setParentResourceValue(pResourceValue);
        CommonResponse<?> commonResponse = authrbacServerApi.moveResourceValue(resourceFsMoveKvReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--资源移动失败",resourceFsMoveKvReq, commonResponse);
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
    }

    @Override
    public void commonDeleteByResourceValue(String resourceKey, String resourceValue) {

        CommonResponse<?> commonResponse = authrbacServerApi.deleteByResourceValue(resourceKey,resourceValue);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--资源删除失败",resourceValue, commonResponse);
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
    }
}
