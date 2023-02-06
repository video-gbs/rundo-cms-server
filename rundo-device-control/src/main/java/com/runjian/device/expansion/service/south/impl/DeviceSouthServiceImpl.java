package com.runjian.device.expansion.service.south.impl;

import com.runjian.device.expansion.constant.DetailType;
import com.runjian.device.expansion.constant.SignState;
import com.runjian.device.expansion.expansion.dao.ChannelMapper;
import com.runjian.device.expansion.expansion.dao.DetailMapper;
import com.runjian.device.expansion.expansion.dao.DeviceMapper;
import com.runjian.device.expansion.entity.DetailInfo;
import com.runjian.device.expansion.entity.DeviceInfo;
import com.runjian.device.expansion.service.south.DeviceSouthService;
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

    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 设备添加注册
     * @param id 设备id
     * @param gatewayId 网关id
     * @param onlineState 在线状态
     * @param deviceType 设备类型
     * @param ip ip地址
     * @param port 端口
     */
    @Override
    public void signIn(Long id, Long gatewayId, Integer onlineState, Integer deviceType, String ip, String port) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(id);
        LocalDateTime nowTime = LocalDateTime.now();
        if (deviceInfoOp.isEmpty()){
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(id);
            deviceInfo.setGatewayId(gatewayId);
            deviceInfo.setDeviceType(deviceType);
            deviceInfo.setOnlineState(onlineState);
            deviceInfo.setCreateTime(nowTime);
            deviceInfo.setUpdateTime(nowTime);
            deviceInfo.setSignState(SignState.TO_BE_ADD.getCode());
            deviceMapper.save(deviceInfo);
        }else {
            DeviceInfo deviceInfo = deviceInfoOp.get();
            deviceInfo.setOnlineState(onlineState);
            deviceInfo.setUpdateTime(nowTime);
            deviceMapper.update(deviceInfo);
            // 修改设备下的通道状态
            channelMapper.updateOnlineStateByDeviceId(id, onlineState);
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
