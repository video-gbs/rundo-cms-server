package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.AuthRbacServerApi;
import com.runjian.device.expansion.feign.AuthServerApi;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.mapper.DeviceChannelExpansionMapper;
import com.runjian.device.expansion.service.IBaseDeviceAndChannelService;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.utils.RedisCommonUtil;
import com.runjian.device.expansion.vo.feign.request.GetCatalogueResourceRsp;
import com.runjian.device.expansion.vo.feign.request.PutChannelSignSuccessReq;
import com.runjian.device.expansion.vo.feign.response.*;
import com.runjian.device.expansion.vo.request.*;
import com.runjian.device.expansion.vo.response.ChannelExpansionFindlistRsp;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionPlayResp;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class IDeviceChannelExpansionServiceImpl extends ServiceImpl<DeviceChannelExpansionMapper, DeviceChannelExpansion> implements IDeviceChannelExpansionService {
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Autowired
    DeviceChannelExpansionMapper deviceChannelExpansionMapper;

    @Autowired
    IDeviceExpansionService deviceExpansionService;

    @Autowired
    DeviceControlApi channelControlApi;

    @Autowired
    AuthServerApi authServerApi;

    @Autowired
    AuthRbacServerApi authRbacServerApi;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IBaseDeviceAndChannelService baseDeviceAndChannelService;

    @Value("${resourceKeys.channelKey:safety_channel}")
    String resourceKey;
    @Override
    public CommonResponse<Boolean> add(FindChannelListReq findChannelListReq) {
        //进行添加
        List<DeviceChannelExpansion> channelList = new ArrayList<>();
        ArrayList<Long> longs = new ArrayList<>();
        for (DeviceChannelExpansionAddReq deviceChannelExpansionAddReq : findChannelListReq.getChannelList()){
            DeviceChannelExpansion deviceChannelExpansion = new DeviceChannelExpansion();
            deviceChannelExpansion.setId(deviceChannelExpansionAddReq.getChannelId());
            deviceChannelExpansion.setDeviceExpansionId(deviceChannelExpansionAddReq.getDeviceExpansionId());
            deviceChannelExpansion.setChannelName(deviceChannelExpansionAddReq.getChannelName());
            deviceChannelExpansion.setChannelCode(deviceChannelExpansionAddReq.getChannelCode());
            deviceChannelExpansion.setOnlineState(deviceChannelExpansionAddReq.getOnlineState());
            deviceChannelExpansion.setVideoAreaId(findChannelListReq.getVideoAreaId());
            channelList.add(deviceChannelExpansion);
            longs.add(deviceChannelExpansionAddReq.getChannelId());
        }
        LambdaQueryWrapper<DeviceChannelExpansion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeviceChannelExpansion::getId,longs);
        List<DeviceChannelExpansion> channelDbs = deviceChannelExpansionMapper.selectList(queryWrapper);
        List<Long> existCollect = channelDbs.stream().map(DeviceChannelExpansion::getId).collect(Collectors.toList());
        for (DeviceChannelExpansion channel : channelList){
            ArrayList<Long> ids = new ArrayList<>();
            ids.add(channel.getId());
            //通知控制服务修改添加状态
            PutChannelSignSuccessReq putChannelSignSuccessReq = new PutChannelSignSuccessReq();
            putChannelSignSuccessReq.setChannelIdList(ids);
            CommonResponse<Boolean> longCommonResponse = channelControlApi.channelSignSuccess(putChannelSignSuccessReq);
            if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",findChannelListReq, longCommonResponse);
                throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, longCommonResponse.getMsg());
            }
            baseDeviceAndChannelService.commonResourceBind(resourceKey,findChannelListReq.getPResourceValue(),channel.getId(),channel.getChannelName());
            if(existCollect.contains(channel.getId())){
                channel.setDeleted(0);
                deviceChannelExpansionMapper.updateById(channel);
            }else {
                this.save(channel);
            }

        }
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> edit(DeviceChannelExpansionReq deviceChannelExpansionReq) {
        DeviceChannelExpansion channelExpansionDb = deviceChannelExpansionMapper.selectById(deviceChannelExpansionReq.getId());
        DeviceChannelExpansion deviceChannelExpansion = new DeviceChannelExpansion();
        BeanUtil.copyProperties(deviceChannelExpansionReq,deviceChannelExpansion);
        //资源修改和移动
        baseDeviceAndChannelService.commonResourceUpdate(resourceKey,String.valueOf(deviceChannelExpansionReq.getId()),deviceChannelExpansionReq.getChannelName());
        baseDeviceAndChannelService.moveResourceByValue(resourceKey,String.valueOf(deviceChannelExpansionReq.getId()),deviceChannelExpansionReq.getPResourceValue());
        deviceChannelExpansionMapper.updateById(deviceChannelExpansion);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> remove(Long id) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            DeviceChannelExpansion channelExpansion = new DeviceChannelExpansion();
            channelExpansion.setDeleted(1);
            channelExpansion.setId(id);
            deviceChannelExpansionMapper.updateById(channelExpansion);
            //通知控制服务修改添加状态 删除接口待定义


            CommonResponse<Boolean> booleanCommonResponse = channelControlApi.channelDeleteSoft(id);
            if(booleanCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, booleanCommonResponse);
                throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, booleanCommonResponse.getMsg());

            }
            dataSourceTransactionManager.commit(transactionStatus);
            baseDeviceAndChannelService.commonDeleteByResourceValue(resourceKey,String.valueOf(id));
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus);
        }

        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> removeBatch(List<Long> idList) {
        for (Long id : idList){
            TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            try {
                DeviceChannelExpansion channelExpansion = new DeviceChannelExpansion();
                channelExpansion.setDeleted(1);
                channelExpansion.setId(id);
                deviceChannelExpansionMapper.updateById(channelExpansion);
                CommonResponse<Boolean> booleanCommonResponse = channelControlApi.channelDeleteSoft(id);
                if(booleanCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                    //调用失败
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",idList, booleanCommonResponse);
                    throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, booleanCommonResponse.getMsg());
                }
                dataSourceTransactionManager.commit(transactionStatus);
                baseDeviceAndChannelService.commonDeleteByResourceValue(resourceKey,String.valueOf(id));
            }catch (Exception e){
                dataSourceTransactionManager.rollback(transactionStatus);
            }



        }

        return CommonResponse.success();
    }

    @Override
    public PageResp<DeviceChannelExpansionResp> list(DeviceChannelExpansionListReq deviceChannelExpansionListReq) {
        //获取安防通道资源
        Long videoAreaId = deviceChannelExpansionListReq.getVideoAreaId();
        Boolean includeEquipment = deviceChannelExpansionListReq.getIncludeEquipment();
        CommonResponse<List<GetCatalogueResourceRsp>> catalogueResourceRsp = authRbacServerApi.getCatalogueResourceRsp(videoAreaId,includeEquipment);
        if(catalogueResourceRsp.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){

            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, catalogueResourceRsp.getMsg());
        }
        List<GetCatalogueResourceRsp> channelList = catalogueResourceRsp.getData();
        ArrayList<Long> longs = new ArrayList<>();
        //获取全部的资源树id
        for (GetCatalogueResourceRsp one : channelList){
            longs.add(Long.parseLong(one.getResourceValue()));
        }
        PageResp<DeviceChannelExpansionResp> listPageResp = new PageResp<>();
        if(ObjectUtils.isEmpty(longs)){
            //数据不存在直接返回
            listPageResp.setCurrent(1);
            listPageResp.setSize(10);
            listPageResp.setTotal(0);
            listPageResp.setRecords(null);
            return listPageResp;
        }

        Page<DeviceChannelExpansion> page = new Page<>(deviceChannelExpansionListReq.getPageNum(), deviceChannelExpansionListReq.getPageSize());
        Page<DeviceChannelExpansionResp> channelExpansionPage = deviceChannelExpansionMapper.listPage(page,deviceChannelExpansionListReq,longs);

        List<DeviceChannelExpansionResp> records = channelExpansionPage.getRecords();
        if(!CollectionUtils.isEmpty(records)){
            for (DeviceChannelExpansionResp channelExpansion: records){
                for (GetCatalogueResourceRsp videoAreaResp: channelList){
                    if(Long.parseLong(videoAreaResp.getResourceValue()) == channelExpansion.getId()){
                        channelExpansion.setAreaNames(videoAreaResp.getLevelName());
                        channelExpansion.setVideoAreaId(videoAreaResp.getParentResourceId());

                    }
                }
            }
        }
        listPageResp.setCurrent(channelExpansionPage.getCurrent());
        listPageResp.setSize(channelExpansionPage.getSize());
        listPageResp.setTotal(channelExpansionPage.getTotal());
        listPageResp.setRecords(records);
        return listPageResp;
    }

    @Override
    public PageResp<ChannelExpansionFindlistRsp> findList(int page, int num, String originName) {
        //获取控制服务的添加通道列表
        CommonResponse<PageListResp<GetChannelByPageRsp>> channelByPage = channelControlApi.getChannelByPage(page, num, originName);
        //封装返回目前数据库中的编码器名称
        if(channelByPage.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--待添加的通道列表获取失败",originName, channelByPage);
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR,channelByPage.getMsg());
        }
        PageResp<ChannelExpansionFindlistRsp> listPageResp = new PageResp<>();
        PageListResp<GetChannelByPageRsp> originPage = channelByPage.getData();
        List<GetChannelByPageRsp> records = originPage.getList();
        List<ChannelExpansionFindlistRsp> channelExpansionFindlistRsps = new ArrayList<>();

        if(!CollectionUtils.isEmpty(records)){
            List<Long> deviceExpansionIds = records.stream().map(GetChannelByPageRsp::getDeviceId).collect(Collectors.toList());
            List<DeviceExpansion> deviceExpansionList = deviceExpansionService.listByIds(deviceExpansionIds);

            for(GetChannelByPageRsp getChannelByPageRsp : records){
                for(DeviceExpansion deviceExpansion : deviceExpansionList){
                    if(getChannelByPageRsp.getDeviceId().equals(deviceExpansion.getId())){
                        ChannelExpansionFindlistRsp channelExpansionFindlistRsp = new ChannelExpansionFindlistRsp();
                        BeanUtil.copyProperties(getChannelByPageRsp,channelExpansionFindlistRsp);
                        channelExpansionFindlistRsp.setDeviceExpansionName(deviceExpansion.getName());
                        channelExpansionFindlistRsp.setChannelCode(getChannelByPageRsp.getOriginId());
                        channelExpansionFindlistRsp.setDeviceExpansionId(getChannelByPageRsp.getDeviceId());
                        channelExpansionFindlistRsps.add(channelExpansionFindlistRsp);
                    }

                }
                
            }
        }
        listPageResp.setCurrent(originPage.getCurrent());
        listPageResp.setSize(originPage.getSize());
        listPageResp.setTotal(originPage.getTotal());
        listPageResp.setRecords(channelExpansionFindlistRsps);
        return listPageResp;
    }

    @Override
    public Boolean move(MoveReq moveReq) {
        DeviceChannelExpansion deviceExpansion = new DeviceChannelExpansion();
        moveReq.getIdList().forEach(id->{
            baseDeviceAndChannelService.moveResourceByValue(resourceKey,String.valueOf(id),moveReq.getPResourceValue());
        });

        return true;
    }

    @Override
    public CommonResponse<ChannelSyncRsp> channelSync(Long deviceId) {

        return channelControlApi.channelSync(deviceId);
    }

    @Override
    public DeviceChannelExpansion findOneDeviceByVideoAreaId(Long areaId) {
        LambdaQueryWrapper<DeviceChannelExpansion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceChannelExpansion::getVideoAreaId,areaId);
        queryWrapper.eq(DeviceChannelExpansion::getDeleted,0).last("limit 1");
        return deviceChannelExpansionMapper.selectOne(queryWrapper);
    }

    @Override
    public void syncChannelStatus(String msgHandle,String msgLock) {
        RLock lock = redissonClient.getLock(msgLock);
        try{
            lock.lock(3, TimeUnit.SECONDS);
            Map<Long, Integer> deviceMap = RedisCommonUtil.hmgetInteger(redisTemplate, msgHandle);
            if(!CollectionUtils.isEmpty(deviceMap)){
                log.info(LogTemplate.PROCESS_LOG_TEMPLATE,"通道缓存状态同步",deviceMap);
                Set<Map.Entry<Long, Integer>> entries = deviceMap.entrySet();
                for(Map.Entry entry:  entries){
                    Long deviceId = (Long)entry.getKey();
                    Integer onlineState = (Integer)entry.getValue();
                    //获取key与value  value为BusinessSceneResp
                    //修改device状态
                    DeviceChannelExpansion channelExpansion = new DeviceChannelExpansion();
                    channelExpansion.setId(deviceId);
                    channelExpansion.setOnlineState(onlineState);
                    deviceChannelExpansionMapper.updateById(channelExpansion);
                }
                RedisCommonUtil.del(redisTemplate,msgHandle);
            }

        } catch (Exception ex){
            log.error(LogTemplate.ERROR_LOG_TEMPLATE,"通道状态更新","更新失败",ex.getMessage());
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        }finally {
            lock.unlock();
        }
    }

    @Override
    public List<DeviceChannelExpansionPlayResp> playList(Long videoAreaId) {
        //获取全部的资源信息
        CommonResponse<List<GetCatalogueResourceRsp>> catalogueResourceRsp = authRbacServerApi.getCatalogueResourceRsp(videoAreaId,false);
        if(catalogueResourceRsp.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){

            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, catalogueResourceRsp.getMsg());
        }
        List<GetCatalogueResourceRsp> channelList = catalogueResourceRsp.getData();
        ArrayList<Long> longs = new ArrayList<>();
        //获取全部的资源树id
        for (GetCatalogueResourceRsp one : channelList){
            longs.add(Long.parseLong(one.getResourceValue()));
        }
        if(ObjectUtils.isEmpty(longs)){
            //没有数据
            return new ArrayList<>();
        }
        LambdaQueryWrapper<DeviceChannelExpansion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeviceChannelExpansion::getId,longs);
        queryWrapper.eq(DeviceChannelExpansion::getDeleted,0);
        queryWrapper.orderByDesc(DeviceChannelExpansion::getCreatedAt);
        queryWrapper.orderByDesc(DeviceChannelExpansion::getOnlineState);
        List<DeviceChannelExpansion> deviceChannelExpansions = deviceChannelExpansionMapper.selectList(queryWrapper);
        ArrayList<DeviceChannelExpansionPlayResp> PlayRespsList = new ArrayList<>();

        if(!ObjectUtils.isEmpty(deviceChannelExpansions)){
            for (DeviceChannelExpansion deviceChannelExpansion: deviceChannelExpansions){
                for(GetCatalogueResourceRsp resourceOne : channelList){
                    long valueLong = Long.parseLong(resourceOne.getResourceValue());
                    if(valueLong == deviceChannelExpansion.getId()){
                        DeviceChannelExpansionPlayResp deviceChannelExpansionPlayResp = new DeviceChannelExpansionPlayResp();
                        BeanUtil.copyProperties(deviceChannelExpansion,deviceChannelExpansionPlayResp);
                        deviceChannelExpansionPlayResp.setChannelId(valueLong);
                        deviceChannelExpansionPlayResp.setId(resourceOne.getResourceId());
                        PlayRespsList.add(deviceChannelExpansionPlayResp);
                    }
                }
            }
        }



        return PlayRespsList;
    }

    @Override
    public CommonResponse<VideoRecordRsp> channelRecord(Long channelId, LocalDateTime startTime, LocalDateTime endTime) {
        return channelControlApi.videoRecordInfo(channelId, startTime, endTime);
    }

    @Override
    public CommonResponse<Object> videoAreaList(String resourceKey) {

        return authRbacServerApi.getResourcePage(resourceKey, false);

    }

    @Override
    public List<DeviceChannelExpansion> batchChannelList(List<Long> channelIds) {
        LambdaQueryWrapper<DeviceChannelExpansion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeviceChannelExpansion::getId,channelIds);
        queryWrapper.eq(DeviceChannelExpansion::getDeleted,0);
        return deviceChannelExpansionMapper.selectList(queryWrapper);
    }
}
