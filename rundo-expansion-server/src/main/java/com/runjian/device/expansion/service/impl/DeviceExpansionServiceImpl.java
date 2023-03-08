package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.AuthServerApi;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.mapper.DeviceExpansionMapper;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.utils.RedisCommonUtil;
import com.runjian.device.expansion.vo.feign.request.PutDeviceSignSuccessReq;
import com.runjian.device.expansion.vo.feign.response.DeviceAddResp;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import com.runjian.device.expansion.vo.request.DeviceExpansionEditReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionListReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
import com.runjian.device.expansion.vo.feign.request.DeviceReq;
import com.runjian.device.expansion.vo.request.MoveReq;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class DeviceExpansionServiceImpl extends ServiceImpl<DeviceExpansionMapper, DeviceExpansion> implements IDeviceExpansionService {
    @Autowired
    DeviceExpansionMapper deviceExpansionMapper;
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    DeviceControlApi deviceControlApi;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    AuthServerApi authServerApi;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Override
    public CommonResponse<DeviceAddResp> add(DeviceExpansionReq deviceExpansionReq) {
        DeviceReq deviceReq = new DeviceReq();
        BeanUtil.copyProperties(deviceExpansionReq,deviceReq);

        CommonResponse<DeviceAddResp> longCommonResponse = deviceControlApi.deviceAdd(deviceReq);
        log.info(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加返回",deviceReq, longCommonResponse);
        if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",deviceReq, longCommonResponse);
            return longCommonResponse;
        }
        DeviceAddResp data = longCommonResponse.getData();

        DeviceExpansion deviceExpansion = new DeviceExpansion();
        BeanUtil.copyProperties(deviceExpansionReq,deviceExpansion);
        deviceExpansion.setId(data.getId());
        deviceExpansion.setOnlineState(data.getOnlineState());
        deviceExpansionMapper.insert(deviceExpansion);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Long> edit(DeviceExpansionEditReq deviceExpansionEditReq) {
        DeviceExpansion deviceExpansionDb = deviceExpansionMapper.selectById(deviceExpansionEditReq.getId());
        if(ObjectUtils.isEmpty(deviceExpansionDb)){
            // 来自待注册列表的数据操作，编辑/恢复
            PutDeviceSignSuccessReq putDeviceSignSuccessReq = new PutDeviceSignSuccessReq();
            putDeviceSignSuccessReq.setDeviceId(deviceExpansionEditReq.getId());
            CommonResponse longCommonResponse = deviceControlApi.deviceSignSuccess(putDeviceSignSuccessReq);
            log.info(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器编辑返回",deviceExpansionEditReq, longCommonResponse);
            if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器状态通知失败",deviceExpansionEditReq, longCommonResponse);
                return longCommonResponse;
            }
            DeviceExpansion deviceExpansion = new DeviceExpansion();
            BeanUtil.copyProperties(deviceExpansionEditReq,deviceExpansion);
            deviceExpansionMapper.insert(deviceExpansion);
        }else {
            DeviceExpansion deviceExpansion = new DeviceExpansion();
            BeanUtil.copyProperties(deviceExpansionEditReq,deviceExpansion);
            deviceExpansionMapper.updateById(deviceExpansion);
        }


        return CommonResponse.success();
    }

    @Override
    public CommonResponse remove(Long id) {
        CommonResponse res = deviceControlApi.deleteDevice(id);
        if(res.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, res);
            return res;
        }

        deviceExpansionMapper.deleteById(id);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> removeBatch(List<Long> idList) {
        for(Long id : idList){
            CommonResponse res = deviceControlApi.deleteDevice(id);
            if(res.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, res);
                return CommonResponse.failure(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
            }else {
                deviceExpansionMapper.deleteById(id);
            }
        }

        return CommonResponse.success();
    }
    @Override
    public  PageResp<DeviceExpansionResp> list(DeviceExpansionListReq deviceExpansionListReq) {
        //远程调用获取当前全部的节点信息
        //安防区域的节点id
        List<Long> areaIdsArr = new ArrayList<>();
        VideoAreaResp videoAreaData = new VideoAreaResp();
        List<VideoAreaResp> videoAreaRespList = new ArrayList<>();
        if(deviceExpansionListReq.getIncludeEquipment()){
            //安防区域不得为空

            CommonResponse<List<VideoAreaResp>> videoAraeList = authServerApi.getVideoAraeList(deviceExpansionListReq.getVideoAreaId());
            if(CollectionUtils.isEmpty(videoAraeList.getData())){
                throw new BusinessException(BusinessErrorEnums.USER_NO_AUTH);

            }
            videoAreaRespList = videoAraeList.getData();
            areaIdsArr = videoAreaRespList.stream().map(VideoAreaResp::getId).collect(Collectors.toList());
        }else {
            if(ObjectUtils.isEmpty(deviceExpansionListReq.getVideoAreaId())){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR);
            }
            CommonResponse<VideoAreaResp> videoAraeInfo = authServerApi.getVideoAraeInfo(deviceExpansionListReq.getVideoAreaId());
            if(ObjectUtils.isEmpty(videoAraeInfo.getData())){
                throw new BusinessException(BusinessErrorEnums.USER_NO_AUTH);

            }
            videoAreaData = videoAraeInfo.getData();
            areaIdsArr.add(videoAreaData.getId());
        }

        LambdaQueryWrapper<DeviceExpansion> queryWrapper = new LambdaQueryWrapper<>();
        if(!ObjectUtils.isEmpty(deviceExpansionListReq.getName())){
            queryWrapper.like(DeviceExpansion::getName,deviceExpansionListReq.getName());
        }
        if(!ObjectUtils.isEmpty(deviceExpansionListReq.getDeviceType())){
            queryWrapper.eq(DeviceExpansion::getDeviceType,deviceExpansionListReq.getDeviceType());
        }
        if(!ObjectUtils.isEmpty(deviceExpansionListReq.getIp())){
            queryWrapper.like(DeviceExpansion::getIp,deviceExpansionListReq.getIp());
        }
        if(!ObjectUtils.isEmpty(deviceExpansionListReq.getOnlineState())){
            queryWrapper.eq(DeviceExpansion::getOnlineState,deviceExpansionListReq.getOnlineState());
        }
        queryWrapper.eq(DeviceExpansion::getDeleted,0);
        queryWrapper.in(DeviceExpansion::getVideoAreaId,areaIdsArr);
        queryWrapper.orderByDesc(DeviceExpansion::getCreatedAt);
        queryWrapper.orderByDesc(DeviceExpansion::getOnlineState);
        Page<DeviceExpansion> page = new Page<>(deviceExpansionListReq.getPageNum(), deviceExpansionListReq.getPageSize());
        Page<DeviceExpansion> deviceExpansionPage = deviceExpansionMapper.selectPage(page, queryWrapper);

        List<DeviceExpansion> records = deviceExpansionPage.getRecords();
        List<DeviceExpansionResp> deviceExpansionRespList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(records)){
            //拼接所属区域
            if(deviceExpansionListReq.getIncludeEquipment()){

                for (DeviceExpansion deviceExpansion: records){
                    DeviceExpansionResp deviceExpansionResp = new DeviceExpansionResp();
                    BeanUtil.copyProperties(deviceExpansion,deviceExpansionResp);
                    for (VideoAreaResp videoAreaResp: videoAreaRespList){
                        if(videoAreaResp.getId().equals(deviceExpansion.getVideoAreaId())){
                            deviceExpansionResp.setAreaNames(videoAreaResp.getAreaNames());

                        }
                    }
                    deviceExpansionRespList.add(deviceExpansionResp);
                }

            }else {
                for (DeviceExpansion deviceExpansion: records){
                    DeviceExpansionResp deviceExpansionResp = new DeviceExpansionResp();
                    BeanUtil.copyProperties(deviceExpansion,deviceExpansionResp);
                    deviceExpansionResp.setAreaNames(videoAreaData.getAreaNames());
                    deviceExpansionRespList.add(deviceExpansionResp);
                }
            }

        }
        PageResp<DeviceExpansionResp> listPageResp = new PageResp<>();
        listPageResp.setCurrent(deviceExpansionPage.getCurrent());
        listPageResp.setSize(deviceExpansionPage.getSize());
        listPageResp.setTotal(deviceExpansionPage.getTotal());
        listPageResp.setRecords(deviceExpansionRespList);
        return listPageResp;
    }

    @Override
    public Boolean move(MoveReq deviceExpansionMoveReq) {
        DeviceExpansion deviceExpansion = new DeviceExpansion();
        deviceExpansionMoveReq.getIdList().forEach(id->{
            deviceExpansion.setVideoAreaId(deviceExpansionMoveReq.getVideoAreaId());
            deviceExpansion.setId(id);
            deviceExpansionMapper.updateById(deviceExpansion);
        });

        return true;
    }

    @Override
    public DeviceExpansion findOneDeviceByVideoAreaId(Long areaId) {
        LambdaQueryWrapper<DeviceExpansion> deviceExpansionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deviceExpansionLambdaQueryWrapper.eq(DeviceExpansion::getVideoAreaId,areaId);
        deviceExpansionLambdaQueryWrapper.eq(DeviceExpansion::getDeleted,0).last("limit 1");
        return deviceExpansionMapper.selectOne(deviceExpansionLambdaQueryWrapper);
    }

    @Override
    public void syncDeviceStatus() {
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_DEVICE_ONLINE_STATE_LOCK);
        try{
            lock.lock(3, TimeUnit.SECONDS);
            Map<Long, Integer> deviceMap = RedisCommonUtil.hmgetInteger(redisTemplate, MarkConstant.REDIS_DEVICE_ONLINE_STATE);
            if(!CollectionUtils.isEmpty(deviceMap)){
                log.info(LogTemplate.PROCESS_LOG_TEMPLATE,"设备缓存状态同步",deviceMap);
                Set<Map.Entry<Long, Integer>> entries = deviceMap.entrySet();
                for(Map.Entry entry:  entries){
                    Long deviceId = (Long)entry.getKey();
                    Integer onlineState = (Integer)entry.getValue();
                    //获取key与value  value为BusinessSceneResp
                    //修改device状态
                    DeviceExpansion deviceExpansion = new DeviceExpansion();
                    deviceExpansion.setId(deviceId);
                    deviceExpansion.setOnlineState(onlineState);
                    deviceExpansionMapper.updateById(deviceExpansion);
                }
                RedisCommonUtil.del(redisTemplate,MarkConstant.REDIS_DEVICE_ONLINE_STATE);
            }

        } catch (Exception ex){
            log.error(LogTemplate.ERROR_LOG_TEMPLATE,"设备状态更新","更新失败",ex.getMessage());
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        }finally {
            lock.unlock();
        }


    }
}
