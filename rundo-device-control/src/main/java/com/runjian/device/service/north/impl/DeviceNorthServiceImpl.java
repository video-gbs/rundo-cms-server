package com.runjian.device.service.north.impl;


import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.*;
import com.runjian.device.constant.Constant;
import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.device.dao.DetailMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.service.common.DataBaseService;
import com.runjian.device.service.common.DetailBaseService;
import com.runjian.device.service.common.RedisBaseService;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.service.north.DeviceNorthService;
import com.runjian.device.vo.feign.DeviceControlReq;
import com.runjian.device.vo.response.DeviceSyncRsp;
import com.runjian.device.vo.response.GetDevicePageRsp;
import com.runjian.device.vo.response.PostDeviceAddRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * 设备北向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceNorthServiceImpl implements DeviceNorthService {

    private final DeviceMapper deviceMapper;

    private final DetailMapper detailMapper;

    private final ParsingEngineApi parsingEngineApi;

    private final DetailBaseService detailBaseService;

    private final ChannelNorthService channelNorthService;

    private final DataBaseService dataBaseService;

    private final RedisBaseService redisBaseService;

    @Override
    public PageInfo<GetDevicePageRsp> getDeviceByPage(int page, int num, Integer signState, String deviceName, String ip) {
        if (Objects.nonNull(signState) && signState.equals(SignState.SUCCESS.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "注册状态不能为已成功");
        }
        PageHelper.startPage(page, num);
        List<GetDevicePageRsp> devicePageRsps = deviceMapper.selectByPage(signState, deviceName, ip);
        return new PageInfo<>(devicePageRsps);
    }

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
    public PostDeviceAddRsp deviceAdd(String originId, Long gatewayId, Integer deviceType, String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType, String username, String password) {
        LocalDateTime nowTime = LocalDateTime.now();

        // 发送注册请求，返回数据ID
        DeviceControlReq req = new DeviceControlReq(gatewayId, IdType.GATEWAY, MsgType.DEVICE_ADD, 15000L);
        req.putData(StandardName.DEVICE_ID, originId);
        req.putData(StandardName.DEVICE_TYPE, deviceType);
        req.putData(StandardName.COM_IP, ip);
        req.putData(StandardName.COM_PORT, port);
        req.putData(StandardName.COM_USERNAME, username);
        req.putData(StandardName.COM_PASSWORD, password);
        CommonResponse<?> response = parsingEngineApi.customEvent(req);
        if (response.isError()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备添加失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        // 获取id
        Long id = (Long) response.getData();
        //判断数据是否存在，存在直接修改注册状态为已添加
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(id);
        if (deviceInfoOp.isPresent()){
            DeviceInfo deviceInfo = deviceInfoOp.get();
            if (deviceInfo.getSignState().equals(SignState.TO_BE_SIGN_IN.getCode())){
                throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "设备已存在，等待注册中");
            }
            if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                return new PostDeviceAddRsp(id, deviceInfo.getOnlineState());
            }
            deviceInfo.setSignState(SignState.SUCCESS.getCode());
            deviceInfo.setUpdateTime(nowTime);
            deviceMapper.updateSignState(deviceInfo);
            Constant.poolExecutor.execute(() -> channelNorthService.channelSync(id));
            return new PostDeviceAddRsp(id, deviceInfo.getOnlineState());
        }
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
        detailBaseService.saveOrUpdateDetail(id, originId, DetailType.DEVICE.getCode(), ip, port, name, manufacturer, model, firmware, ptzType, nowTime, username, password);
        return new PostDeviceAddRsp(id, deviceInfo.getOnlineState());
    }


    /**
     * 设备注册成功状态
     * @param deviceId 设备id
     */
    @Override
    public void deviceSignSuccess(Long deviceId) {
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(deviceId);
        if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "该设备注册状态已是成功状态");
        }
        deviceInfo.setSignState(SignState.SUCCESS.getCode());
        deviceInfo.setUpdateTime(LocalDateTime.now());
        // 修改设备注册状态
        deviceMapper.updateSignState(deviceInfo);
        redisBaseService.updateDeviceOnlineState(deviceInfo.getId(), deviceInfo.getOnlineState());
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
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(deviceId);
        // 判断是否设备在线
        if (deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s处于离线状态", deviceId));
        }
        // 判断设备是否处于删除状态
        if (deviceInfo.getSignState().equals(SignState.DELETED.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s处于删除状态", deviceId));
        }
        // 请求解析引擎，进行设备同步
        CommonResponse<?> response = parsingEngineApi.customEvent(new DeviceControlReq(deviceId, IdType.DEVICE, MsgType.DEVICE_SYNC, 15000L));
        if (response.isError()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备同步失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        DeviceSyncRsp data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()), DeviceSyncRsp.class);
        LocalDateTime nowTime = LocalDateTime.now();
        detailBaseService.saveOrUpdateDetail(deviceId, null,  DetailType.DEVICE.getCode(), data.getIp(), data.getPort(), data.getName(), data.getManufacturer(), data.getModel(), data.getFirmware(), data.getPtzType(), nowTime, data.getUsername(), data.getPassword());
        return data;
    }

    /**
     * 删除设备
     * @param deviceId 设备id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deviceDelete(Long deviceId) {
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(deviceId);
        // 触发删除流程，返回boolean
        CommonResponse<?> response = parsingEngineApi.customEvent(new DeviceControlReq(deviceId, IdType.DEVICE, MsgType.DEVICE_DELETE, 15000L));
        if (response.isError()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备北向服务", "设备删除失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }

        // 判断网关是否删除成功
        if ((Boolean) response.getData()){
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
