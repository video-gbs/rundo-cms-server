package com.runjian.device.expansion.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.constant.Constant;
import com.runjian.device.expansion.constant.DetailType;
import com.runjian.device.expansion.constant.SignState;
import com.runjian.device.expansion.expansion.dao.DetailMapper;
import com.runjian.device.expansion.expansion.dao.DeviceMapper;
import com.runjian.device.expansion.entity.DeviceInfo;
import com.runjian.device.expansion.feign.ParsingEngineApi;
import com.runjian.device.expansion.service.DetailBaseService;
import com.runjian.device.expansion.service.north.ChannelNorthService;
import com.runjian.device.expansion.service.north.DeviceNorthService;
import com.runjian.device.expansion.vo.feign.DeviceControlReq;
import com.runjian.device.expansion.vo.response.DeviceSyncRsp;
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

    @Autowired
    private ChannelNorthService channelNorthService;

    /**
     * 设备主动添加
     * @param originId 设备原始ID
     * @param gatewayId 网关ID
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
    public void deviceAdd(String originId, Long gatewayId, Integer deviceType, String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType, String username, String password) {
        LocalDateTime nowTime = LocalDateTime.now();
        // 发送注册请求，返回数据ID

        DeviceControlReq req = new DeviceControlReq();
        req.setGatewayId(gatewayId);
        req.putData("deviceId", originId);
        req.putData("deviceType", deviceType);
        req.putData("ip", ip);
        req.putData("port", port);
        req.putData("username", username);
        req.putData("password", password);
        CommonResponse<Long> response = parsingEngineApi.deviceAdd(req);
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
        deviceInfo.setOnlineState(CommonEnum.DISABLE.getCode());
        deviceInfo.setCreateTime(nowTime);
        deviceInfo.setUpdateTime(nowTime);
        // 设置状态为待注册
        deviceInfo.setSignState(SignState.TO_BE_SIGN_IN.getCode());
        deviceMapper.save(deviceInfo);
        // 保存详细信息
        detailBaseService.saveOrUpdateDetail(id, DetailType.DEVICE.getCode(), ip, port, name, manufacturer, model, firmware, ptzType, nowTime);
    }

    /**
     * 设备注册成功状态
     * @param deviceId 设备id
     */
    @Override
    public void deviceSignSuccess(Long deviceId) {
        DeviceInfo deviceInfo = getDeviceInfo(deviceId);
        if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "该设备注册状态已是成功状态");
        }
        deviceInfo.setSignState(SignState.SUCCESS.getCode());
        deviceInfo.setUpdateTime(LocalDateTime.now());
        // 修改设备注册状态
        deviceMapper.updateSignState(deviceInfo);
        // 异步触发通道同步
        Constant.poolExecutor.execute(() -> channelNorthService.channelSync(deviceId));
    }


    /**
     * 设备同步
     * @param deviceId 设备id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceSyncRsp deviceSync(Long deviceId) {
        DeviceInfo deviceInfo = getDeviceInfo(deviceId);
        // 判断是否设备在线
        if (deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s处于离线状态", deviceId));
        }
        // 判断设备是否处于删除状态
        if (deviceInfo.getSignState().equals(SignState.DELETED.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s处于删除状态", deviceId));
        }
        // 请求解析引擎，进行设备同步
        CommonResponse<DeviceSyncRsp> response = parsingEngineApi.deviceSync(deviceId);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备同步失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        DeviceSyncRsp data = response.getData();
        LocalDateTime nowTime = LocalDateTime.now();
        detailBaseService.saveOrUpdateDetail(deviceId, DetailType.DEVICE.getCode(), data.getIp(), data.getPort(), data.getName(), data.getManufacturer(), data.getModel(), data.getFirmware(), data.getPtzType(), nowTime);
        return data;
    }


    /**
     * 获取设备信息
     * @param deviceId 设备id
     * @return
     */
    private DeviceInfo getDeviceInfo(Long deviceId) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(deviceId);
        // 判断是否存在的数据
        if (deviceInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "设备id:" + deviceId);
        }
        DeviceInfo deviceInfo = deviceInfoOp.get();

        return deviceInfo;
    }

    /**
     * 删除设备
     * @param deviceId 设备id
     */
    @Override
    public void deviceDelete(Long deviceId) {
        DeviceInfo deviceInfo = getDeviceInfo(deviceId);
        // 触发删除流程，返回boolean
        CommonResponse<Boolean> response = parsingEngineApi.deviceDelete(deviceId);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备删除失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        Boolean isDeleted = response.getData();

        // 判断网关是否删除成功
        if (isDeleted){
            // 若删除成功，删除所有数据
            detailMapper.deleteByDcIdAndType(deviceInfo.getId(), DetailType.DEVICE.getCode());
            deviceMapper.deleteById(deviceInfo.getId());
            // 删除通道信息
            channelNorthService.channelDeleteByDeviceId(deviceInfo.getId(), true);
        }else {
            // 删除失败，将数据设置为删除状态
            deviceInfo.setSignState(SignState.DELETED.getCode());
            deviceInfo.setUpdateTime(LocalDateTime.now());
            deviceMapper.update(deviceInfo);
            channelNorthService.channelDeleteByDeviceId(deviceInfo.getId(), false);
        }
    }
}
