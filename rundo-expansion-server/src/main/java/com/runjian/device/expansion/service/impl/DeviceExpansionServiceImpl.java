package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.mapper.DeviceExpansionMapper;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.request.DeviceExpansionEditReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
import com.runjian.device.expansion.vo.request.feign.DeviceReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public CommonResponse<Long> add(DeviceExpansionReq deviceExpansionReq) {
        DeviceReq deviceReq = new DeviceReq();
        BeanUtil.copyProperties(deviceExpansionReq,deviceReq);

        CommonResponse<Long> longCommonResponse = deviceControlApi.deviceAdd(deviceReq);
        if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",deviceReq, longCommonResponse);
            return longCommonResponse;
        }
        DeviceExpansion deviceExpansion = new DeviceExpansion();
        BeanUtil.copyProperties(deviceExpansionReq,deviceExpansion);

        deviceExpansion.setId(longCommonResponse.getData());
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
}
