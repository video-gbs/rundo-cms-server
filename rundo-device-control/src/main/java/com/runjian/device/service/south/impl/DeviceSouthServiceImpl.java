package com.runjian.device.service.south.impl;

import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.device.dao.DetailMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.service.south.DeviceSouthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 设备南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Service
public class DeviceSouthServiceImpl implements DeviceSouthService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DetailMapper detailMapper;
    @Override
    public void signIn(Long id, Long gatewayId, Integer online, Integer deviceType, String ip, String port) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(id);
        LocalDateTime nowTime = LocalDateTime.now();
        if (deviceInfoOp.isEmpty()){
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(id);
            deviceInfo.setGatewayId(gatewayId);
            deviceInfo.setDeviceType(deviceType);
            deviceInfo.setOnlineState(online);
            deviceInfo.setCreateTime(nowTime);
            deviceInfo.setUpdateTime(nowTime);
            deviceInfo.setSignState(SignState.TO_BE_ADD.getCode());
            deviceMapper.save(deviceInfo);
        }else {
            DeviceInfo deviceInfo = deviceInfoOp.get();
            deviceInfo.setOnlineState(online);
            deviceInfo.setUpdateTime(nowTime);
            deviceMapper.update(deviceInfo);
        }

        Optional<DetailInfo> detailInfoOp = detailMapper.selectByDcIdAndType(id, DetailType.DEVICE.getCode());
        if (detailInfoOp.isEmpty()){
            DetailInfo detailInfo = new DetailInfo();
            detailInfo.setIp(ip);
            detailInfo.setPort(port);
            detailInfo.setUpdateTime(nowTime);
            detailInfo.setDcId(id);
            detailInfo.setType(DetailType.DEVICE.getCode());
            detailInfo.setCreateTime(nowTime);
            detailMapper.save(detailInfo);
        } else {
            DetailInfo detailInfo = detailInfoOp.get();
            detailInfo.setIp(ip);
            detailInfo.setPort(port);
            detailInfo.setUpdateTime(nowTime);
            detailMapper.update(detailInfo);
        }
    }

}
