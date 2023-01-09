package com.runjian.device.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.device.dao.DetailMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.service.DetailBaseService;
import com.runjian.device.service.north.DeviceNorthService;
import com.runjian.device.vo.response.DeviceSyncRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * 设备北向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Slf4j
@Service
public class DeviceNorthServiceImpl implements DeviceNorthService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DetailMapper detailMapper;

    @Autowired
    private ParsingEngineApi parsingEngineApi;

    @Autowired
    private DetailBaseService detailBaseService;

    /**
     * 设备主动添加
     * @param deviceId 设备原始ID
     * @param gatewayId 网关ID
     * @param online 在线与离线状态
     * @param deviceType 设备类型
     * @param ip ip地址
     * @param port 端口
     * @param name 设备名称
     * @param manufacturer 厂商
     * @param model 型号
     * @param firmware 固件版本
     * @param ptzType 云台类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deviceAdd(String deviceId, Long gatewayId, Integer online, Integer deviceType, String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectByOriginIdAndGatewayId(deviceId, gatewayId);
        LocalDateTime nowTime = LocalDateTime.now();
        // 判断数据是否存在
        if (deviceInfoOp.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("设备%s在网关%s已存在", deviceId, gatewayId));
        }
        // 发送注册请求，返回数据ID
        CommonResponse<Long> response = parsingEngineApi.deviceAdd(deviceId, gatewayId);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备注册失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        // 获取id
        Long id =  response.getData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        deviceInfo.setGatewayId(gatewayId);
        deviceInfo.setDeviceType(deviceType);
        deviceInfo.setOnlineState(online);
        deviceInfo.setCreateTime(nowTime);
        deviceInfo.setUpdateTime(nowTime);
        // 设置状态为待注册
        deviceInfo.setSignState(SignState.TO_BE_SIGN_IN.getCode());
        deviceMapper.save(deviceInfo);
        // 保存详细信息
        detailBaseService.saveOrUpdateDetail(id, DetailType.DEVICE.getCode(), ip, port, name, manufacturer, model, firmware, ptzType, nowTime);
    }



    /**
     * 设备同步
     * @param id 设备id
     * @return
     */
    @Override
    public DeviceSyncRsp deviceSync(Long id) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(id);
        // 判断是否存在的数据
        if (deviceInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "设备id:" + id);
        }
        DeviceInfo deviceInfo = deviceInfoOp.get();
        // 判断是否设备在线
        if (deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s处于离线状态", id));
        }
        // 判断设备是否处于删除状态
        if (deviceInfo.getSignState().equals(SignState.DELETED.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s处于删除状态", id));
        }
        // 请求解析引擎，进行设备同步
        CommonResponse<DeviceSyncRsp> response = parsingEngineApi.deviceSync(id);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备同步失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        DeviceSyncRsp data = response.getData();
        LocalDateTime nowTime = LocalDateTime.now();
        detailBaseService.saveOrUpdateDetail(id, DetailType.DEVICE.getCode(), data.getIp(), data.getPort(), data.getName(), data.getManufacturer(), data.getModel(), data.getFirmware(), data.getPtzType(), nowTime);
        return data;
    }

    /**
     * 删除设备
     * @param id 设备id
     */
    @Override
    public void delDevice(Long id) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(id);
        if (deviceInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "设备id:" + id);
        }
        // 触发删除流程，返回boolean
        CommonResponse<Boolean> response = parsingEngineApi.deviceDelete(id);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备删除失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        Boolean isDeleted = response.getData();
        DeviceInfo deviceInfo = deviceInfoOp.get();
        // 判断网关是否删除成功
        if (isDeleted){
            // 若删除成功，删除所有数据
            deviceMapper.deleteById(deviceInfo.getId());
            detailMapper.deleteByDcIdAndType(deviceInfo.getId(), DetailType.DEVICE.getCode());
        }else {
            // 删除失败，将数据设置为删除状态
            deviceInfo.setSignState(SignState.DELETED.getCode());
            deviceInfo.setUpdateTime(LocalDateTime.now());
            deviceMapper.update(deviceInfo);
        }
    }
}
