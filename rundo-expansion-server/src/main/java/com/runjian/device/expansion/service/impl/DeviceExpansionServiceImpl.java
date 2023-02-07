package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.AuthServerApi;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.mapper.DeviceExpansionMapper;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import com.runjian.device.expansion.vo.request.DeviceExpansionEditReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionListReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionMoveReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
import com.runjian.device.expansion.vo.feign.request.DeviceReq;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
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
    DeviceControlApi deviceControlApi;

    @Autowired
    AuthServerApi authServerApi;
    @Override
    public CommonResponse<Long> add(DeviceExpansionReq deviceExpansionReq) {
        DeviceReq deviceReq = new DeviceReq();
        BeanUtil.copyProperties(deviceExpansionReq,deviceReq);

//        CommonResponse<Long> longCommonResponse = deviceControlApi.deviceAdd(deviceReq);
//        if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
//            //调用失败
//            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",deviceReq, longCommonResponse);
//            return longCommonResponse;
//        }
        DeviceExpansion deviceExpansion = new DeviceExpansion();
        BeanUtil.copyProperties(deviceExpansionReq,deviceExpansion);

//        deviceExpansion.setId(longCommonResponse.getData());
        deviceExpansionMapper.insert(deviceExpansion);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Long> edit(DeviceExpansionEditReq deviceExpansionEditReq) {
        DeviceExpansion deviceExpansion = new DeviceExpansion();
        BeanUtil.copyProperties(deviceExpansionEditReq,deviceExpansion);
        deviceExpansionMapper.updateById(deviceExpansion);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse remove(Long id) {
        deviceExpansionMapper.deleteById(id);
        CommonResponse res = deviceControlApi.deleteDevice(id);
        if(res.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, res);
        }

        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> removeBatch(List<Long> idList) {
        deviceExpansionMapper.deleteBatchIds(idList);
        idList.forEach(id->{
            CommonResponse res = deviceControlApi.deleteDevice(id);
            if(res.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, res);
            }
        });
        return CommonResponse.success();
    }

    @Override
    public List<DeviceExpansionResp> list(DeviceExpansionListReq deviceExpansionListReq) {
        //远程调用获取当前全部的节点信息
        //安防区域的节点id
        List<Long> areaIdsArr = new ArrayList<>();
        VideoAreaResp videoAreaData = new VideoAreaResp();
        List<VideoAreaResp> videoAreaRespList = new ArrayList<>();
        if(deviceExpansionListReq.getIncludeEquipment()){
            //安防区域不得为空
            if(ObjectUtils.isEmpty(deviceExpansionListReq.getVideoAreaId())){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR);
            }
            CommonResponse<List<VideoAreaResp>> videoAraeList = authServerApi.getVideoAraeList(deviceExpansionListReq.getVideoAreaId());
            if(CollectionUtils.isEmpty(videoAraeList.getData())){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR);

            }
            videoAreaRespList = videoAraeList.getData();
            areaIdsArr = videoAreaRespList.stream().map(VideoAreaResp::getId).collect(Collectors.toList());
        }else {
            CommonResponse<VideoAreaResp> videoAraeInfo = authServerApi.getVideoAraeInfo(deviceExpansionListReq.getVideoAreaId());
            if(ObjectUtils.isEmpty(videoAraeInfo.getData())){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR);

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
        Page<DeviceExpansion> page = new Page<>(deviceExpansionListReq.getPageNum(), deviceExpansionListReq.getPageSize());
        Page<DeviceExpansion> deviceExpansionPage = deviceExpansionMapper.selectPage(page, queryWrapper);
        List<DeviceExpansion> records = deviceExpansionPage.getRecords();
        ArrayList<DeviceExpansionResp> deviceExpansionRespList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(records)){
            //拼接所属区域
            DeviceExpansionResp deviceExpansionResp = new DeviceExpansionResp();
            if(deviceExpansionListReq.getIncludeEquipment()){

                for (DeviceExpansion deviceExpansion: records){
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
                    BeanUtil.copyProperties(deviceExpansion,deviceExpansionResp);
                    deviceExpansionResp.setAreaNames(videoAreaData.getAreaNames());
                    deviceExpansionRespList.add(deviceExpansionResp);
                }
            }

        }
        return deviceExpansionRespList;
    }

    @Override
    public Boolean move(DeviceExpansionMoveReq deviceExpansionMoveReq) {
        DeviceExpansion deviceExpansion = new DeviceExpansion();
        deviceExpansionMoveReq.getIdList().forEach(id->{
            deviceExpansion.setVideoAreaId(deviceExpansionMoveReq.getVideoAreaId());
            deviceExpansion.setId(id);
            deviceExpansionMapper.updateById(deviceExpansion);
        });

        return true;
    }
}
