package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.AuthRbacServerApi;
import com.runjian.device.expansion.feign.AuthServerApi;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.mapper.DeviceExpansionMapper;
import com.runjian.device.expansion.service.IBaseDeviceAndChannelService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.utils.RedisCommonUtil;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.DeviceAddResp;
import com.runjian.device.expansion.vo.feign.response.GetResourceTreeRsp;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResourceRsp;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import com.runjian.device.expansion.vo.request.DeviceExpansionEditReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionListReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
import com.runjian.device.expansion.vo.request.MoveReq;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
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
    AuthRbacServerApi authrbacServerApi;

    @Autowired
    IBaseDeviceAndChannelService baseDeviceAndChannelService;

    @Value("${resourceKeys.deviceKey:safety_device}")
    String resourceKey;

    @Override
    public CommonResponse<DeviceAddResp> add(DeviceExpansionReq deviceExpansionReq) {
        DeviceReq deviceReq = new DeviceReq();
        BeanUtil.copyProperties(deviceExpansionReq,deviceReq);

        CommonResponse<DeviceAddResp> longCommonResponse = deviceControlApi.deviceAdd(deviceReq);
        log.info(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加返回",deviceReq, longCommonResponse);
        if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",deviceReq, longCommonResponse);
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, longCommonResponse.getMsg());
        }
        DeviceAddResp data = longCommonResponse.getData();
        Long encoderId = data.getId();
        DeviceExpansion oldOne = this.getById(encoderId);

        if(!ObjectUtils.isEmpty(oldOne)){
            throw new BusinessException(BusinessErrorEnums.DATA_ALREADY_EXISTED,"数据已存在，请勿重复添加");
        }
        baseDeviceAndChannelService.commonResourceBind(resourceKey,deviceExpansionReq.getPResourceValue(),encoderId,deviceExpansionReq.getName());
        DeviceExpansion deviceExpansion = new DeviceExpansion();
        BeanUtil.copyProperties(deviceExpansionReq,deviceExpansion);
        deviceExpansion.setId(data.getId());
        deviceExpansion.setOnlineState(data.getOnlineState());
        deviceExpansionMapper.insert(deviceExpansion);



        return CommonResponse.success();
    }


    @Override
    public CommonResponse<Long> edit(DeviceExpansionEditReq deviceExpansionEditReq,int kind) {
        DeviceExpansion deviceExpansionDb = deviceExpansionMapper.selectById(deviceExpansionEditReq.getId());
        //恢复与编辑
        if(kind ==1){
            //恢复
            baseDeviceAndChannelService.commonResourceBind(resourceKey,deviceExpansionEditReq.getPResourceValue(),deviceExpansionEditReq.getId(),deviceExpansionEditReq.getName());

        }else {
            baseDeviceAndChannelService.commonResourceUpdate(resourceKey,String.valueOf(deviceExpansionEditReq.getId()),deviceExpansionEditReq.getName());

        }
        baseDeviceAndChannelService.moveResourceByValue(resourceKey,String.valueOf(deviceExpansionEditReq.getId()),deviceExpansionEditReq.getPResourceValue());


        if(ObjectUtils.isEmpty(deviceExpansionDb)){
            // 来自待注册列表的数据操作，编辑/恢复
            PutDeviceSignSuccessReq putDeviceSignSuccessReq = new PutDeviceSignSuccessReq();
            putDeviceSignSuccessReq.setDeviceId(deviceExpansionEditReq.getId());
            CommonResponse longCommonResponse = deviceControlApi.deviceSignSuccess(putDeviceSignSuccessReq);
            log.info(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器编辑返回",deviceExpansionEditReq, longCommonResponse);
            if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器状态通知失败",deviceExpansionEditReq, longCommonResponse);
                throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, longCommonResponse.getMsg());
            }
            DeviceExpansion deviceExpansion = new DeviceExpansion();
            BeanUtil.copyProperties(deviceExpansionEditReq,deviceExpansion);
            deviceExpansionMapper.insert(deviceExpansion);
        }else {


            DeviceExpansion deviceExpansion = new DeviceExpansion();
            if(deviceExpansionDb.getDeleted() != 0){
                //设备恢复
                PutDeviceSignSuccessReq putDeviceSignSuccessReq = new PutDeviceSignSuccessReq();
                putDeviceSignSuccessReq.setDeviceId(deviceExpansionEditReq.getId());
                CommonResponse longCommonResponse = deviceControlApi.deviceSignSuccess(putDeviceSignSuccessReq);
                log.info(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器编辑返回",deviceExpansionEditReq, longCommonResponse);
                if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                    //调用失败
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器状态通知失败",deviceExpansionEditReq, longCommonResponse);
                    return longCommonResponse;
                }
            }
            BeanUtil.copyProperties(deviceExpansionEditReq,deviceExpansion);
            deviceExpansion.setDeleted(0);
            deviceExpansionMapper.updateById(deviceExpansion);
        }


        return CommonResponse.success();
    }

    @Override
    public CommonResponse remove(Long id) {
        CommonResponse res = deviceControlApi.deleteDeviceSoft(id);
        if(res.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, res);
            return res;
        }
        baseDeviceAndChannelService.removeDeviceSoft(id);

        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> removeBatch(List<Long> idList) {
        for(Long id : idList){
            CommonResponse res = deviceControlApi.deleteDeviceSoft(id);
            if(res.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, res);
                throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR,res.getMsg());
            }else {
                baseDeviceAndChannelService.removeDeviceSoft(id);

            }
        }

        return CommonResponse.success();
    }
    @Override
    public  PageResp<DeviceExpansionResp> list(DeviceExpansionListReq deviceExpansionListReq) {
        //获取安防通道资源
        Long videoAreaId = deviceExpansionListReq.getVideoAreaId();
        Boolean includeEquipment = deviceExpansionListReq.getIncludeEquipment();
        VideoAreaResourceRsp videoAreaResourceRsp = baseDeviceAndChannelService.resourceIdList(videoAreaId, includeEquipment);
        List<Long> longs = videoAreaResourceRsp.getChannelData();
        PageResp<DeviceExpansionResp> listPageResp = new PageResp<>();
        if(ObjectUtils.isEmpty(longs)){
            //数据不存在直接返回
            listPageResp.setCurrent(1);
            listPageResp.setSize(10);
            listPageResp.setTotal(0);
            listPageResp.setRecords(null);
            return listPageResp;
        }

        List<GetCatalogueResourceRsp> dataList = videoAreaResourceRsp.getDataList();
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
        queryWrapper.in(DeviceExpansion::getId,longs);
        queryWrapper.orderByDesc(DeviceExpansion::getCreatedAt);
        queryWrapper.orderByDesc(DeviceExpansion::getOnlineState);
        Page<DeviceExpansion> page = new Page<>(deviceExpansionListReq.getPageNum(), deviceExpansionListReq.getPageSize());
        Page<DeviceExpansion> deviceExpansionPage = deviceExpansionMapper.selectPage(page, queryWrapper);

        List<DeviceExpansion> records = deviceExpansionPage.getRecords();
        List<DeviceExpansionResp> deviceExpansionRespList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(records)){
            //拼接所属区域
            for (DeviceExpansion deviceExpansion: records){
                DeviceExpansionResp deviceExpansionResp = new DeviceExpansionResp();
                BeanUtil.copyProperties(deviceExpansion,deviceExpansionResp);
                for (GetCatalogueResourceRsp videoAreaResp: dataList){
                    if(Long.parseLong(videoAreaResp.getResourceValue()) == deviceExpansion.getId()){
                        deviceExpansionResp.setAreaNames(videoAreaResp.getLevelName());
                        deviceExpansionResp.setVideoAreaId(videoAreaResp.getParentResourceId());
                    }
                }
                deviceExpansionRespList.add(deviceExpansionResp);
            }


        }
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
            baseDeviceAndChannelService.moveResourceByValue(resourceKey,String.valueOf(id),deviceExpansionMoveReq.getPResourceValue());
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
    public void syncDeviceStatus(String msgHandle,String msgLock) {
        RLock lock = redissonClient.getLock(msgLock);
        try{
            lock.lock(3, TimeUnit.SECONDS);
            Map<Long, Integer> deviceMap = RedisCommonUtil.hmgetInteger(redisTemplate, msgHandle);
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
                RedisCommonUtil.del(redisTemplate,msgHandle);
            }

        } catch (Exception ex){
            log.error(LogTemplate.ERROR_LOG_TEMPLATE,"设备状态更新","更新失败",ex.getMessage());
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        }finally {
            lock.unlock();
        }


    }

    @Override
    public Object getDeviceByPage(int page, int num, Integer signState, String deviceName, String ip) {
        CommonResponse<Object> deviceByPage = deviceControlApi.getDeviceByPage(page, num, signState, deviceName, ip);
        if(deviceByPage.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, deviceByPage.getMsg());
        }

        return deviceByPage.getData();
    }


    @Override
    public CommonResponse<Object> videoAreaList(String resourceKey) {


        return authrbacServerApi.getResourcePage(resourceKey, false);

    }
}
