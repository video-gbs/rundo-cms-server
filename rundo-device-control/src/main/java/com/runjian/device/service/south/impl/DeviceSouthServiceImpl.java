package com.runjian.device.service.south.impl;

import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DetailMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.service.south.DeviceSouthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private ChannelNorthService channelNorthService;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
    public void signIn(Long id, Long gatewayId, String originId, Integer onlineState, Integer deviceType, String ip, String port) {
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
            // 修改设备状态
            deviceInfo.setUpdateTime(nowTime);
            // 设备从离线到在线，进行通道同步
            if (onlineState.equals(CommonEnum.ENABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
                // 对通道同步
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    // todo 上锁开始
                    redisTemplate.opsForHash().put(MarkConstant.REDIS_DEVICE_ONLINE_STATE, deviceInfo.getId(), deviceInfo.getOnlineState());
                    // todo 上锁结束
                    channelNorthService.channelSync(deviceInfo.getId());
                }
            } else if (onlineState.equals(CommonEnum.DISABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.ENABLE.getCode())) {
                // 将通道全部离线
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    redisTemplate.opsForHash().put(MarkConstant.REDIS_DEVICE_ONLINE_STATE, deviceInfo.getId(), deviceInfo.getOnlineState());
                }
                channelMapper.updateOnlineStateByDeviceId(id, onlineState, nowTime);
            }
        }

        Optional<DetailInfo> detailInfoOp = detailMapper.selectByDcIdAndType(id, DetailType.DEVICE.getCode());
        if (detailInfoOp.isEmpty()){
            DetailInfo detailInfo = new DetailInfo();
            detailInfo.setOriginId(originId);
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
