package com.runjian.device.service.south.impl;


import com.runjian.common.constant.CommonEnum;
import com.runjian.device.constant.Constant;
import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.service.common.DetailBaseService;
import com.runjian.device.service.common.RedisBaseService;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.service.south.DeviceSouthService;
import com.runjian.device.vo.request.PostDeviceSignInReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Service
@RequiredArgsConstructor
public class DeviceSouthServiceImpl implements DeviceSouthService {


    private final DeviceMapper deviceMapper;


    private final ChannelMapper channelMapper;


    private final ChannelNorthService channelNorthService;


    private final RedisBaseService redisBaseService;


    private final DetailBaseService detailBaseService;

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
    public void signIn(Long id, Long gatewayId, String originId, Integer onlineState, Integer deviceType, String ip, String port,
                       String name, String manufacturer, String model, String firmware, Integer ptzType, String username, String password) {
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
            if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                redisBaseService.updateDeviceOnlineState(deviceInfo.getId(), deviceInfo.getOnlineState());
            }
        }else {
            DeviceInfo deviceInfo = deviceInfoOp.get();
            deviceInfo.setUpdateTime(nowTime);
            boolean isAutoSignIn = false;
            // 判断是否是待注册状态
            if (deviceInfo.getSignState().equals(SignState.TO_BE_SIGN_IN.getCode())){
                // 注册成功
                deviceInfo.setSignState(SignState.SUCCESS.getCode());
                isAutoSignIn = true;
            }

            // 设备从离线到在线，进行通道同步
            if (onlineState.equals(CommonEnum.ENABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
                // 对通道同步
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    redisBaseService.updateDeviceOnlineState(deviceInfo.getId(), deviceInfo.getOnlineState());
                    Constant.poolExecutor.execute(() -> channelNorthService.channelSync(deviceInfo.getId()));
                }
            } else if (onlineState.equals(CommonEnum.DISABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.ENABLE.getCode())) {
                // 将通道全部离线
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                channelMapper.updateOnlineStateByDeviceId(id, onlineState, nowTime);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    redisBaseService.updateDeviceOnlineState(deviceInfo.getId(), CommonEnum.DISABLE.getCode());
                    Map<Long, Integer> channelInfoMap = channelMapper.selectByDeviceIdAndSignState(deviceInfo.getId(), SignState.SUCCESS.getCode()).stream().collect(Collectors.toMap(ChannelInfo::getId, channelInfo -> CommonEnum.DISABLE.getCode()));
                    redisBaseService.batchUpdateChannelOnlineState(channelInfoMap);
                }
            } else if (isAutoSignIn && onlineState.equals(CommonEnum.ENABLE.getCode())){
                deviceInfo.setOnlineState(onlineState);
                deviceMapper.update(deviceInfo);
                redisBaseService.updateDeviceOnlineState(deviceInfo.getId(), deviceInfo.getOnlineState());
                Constant.poolExecutor.execute(() -> channelNorthService.channelSync(deviceInfo.getId()));
            }
        }
        detailBaseService.saveOrUpdateDetail(id, originId, DetailType.DEVICE.getCode(), ip, port, name, manufacturer, model, firmware, ptzType, nowTime, username, password);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signInBatch(List<PostDeviceSignInReq> req) {
        if (req.isEmpty()){
            return;
        }
        Map<Long, PostDeviceSignInReq> postDeviceSignInReqMap = req.stream().collect(Collectors.toMap(PostDeviceSignInReq::getDeviceId, postDeviceSignInReq -> postDeviceSignInReq));
        // 查询已存在的设备信息
        List<DeviceInfo> oldDeviceInfoList = deviceMapper.selectByIds(postDeviceSignInReqMap.keySet());

        // 初始化批量处理容器
        List<DeviceInfo> updateDeviceList = new ArrayList<>(oldDeviceInfoList.size());
        List<DeviceInfo> saveDeviceList = new ArrayList<>(postDeviceSignInReqMap.size() - oldDeviceInfoList.size());
        List<DetailInfo> detailInfoList = new ArrayList<>(postDeviceSignInReqMap.size());
        List<Long> offLineDeviceIdList = new ArrayList<>(oldDeviceInfoList.size());
        List<Long> needChannelSyncDevice = new ArrayList<>(oldDeviceInfoList.size());
        Map<Long, Integer> updateDeviceRedisMap = new HashMap<>(postDeviceSignInReqMap.size());

        LocalDateTime nowTime = LocalDateTime.now();
        // 修改
        for (DeviceInfo deviceInfo : oldDeviceInfoList){
            PostDeviceSignInReq postDeviceSignInReq = postDeviceSignInReqMap.remove(deviceInfo.getId());
            deviceInfo.setUpdateTime(nowTime);
            boolean isAutoSignIn = false;
            // 判断是否是待注册状态
            if (deviceInfo.getSignState().equals(SignState.TO_BE_SIGN_IN.getCode())){
                // 注册成功
                deviceInfo.setSignState(SignState.SUCCESS.getCode());
                isAutoSignIn = true;
            }

            // 设备从离线到在线，进行通道同步
            if (postDeviceSignInReq.getOnlineState().equals(CommonEnum.ENABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
                // 对通道同步
                deviceInfo.setOnlineState(postDeviceSignInReq.getOnlineState());
                updateDeviceList.add(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
                    needChannelSyncDevice.add(deviceInfo.getId());
                }
            } else if (postDeviceSignInReq.getOnlineState().equals(CommonEnum.DISABLE.getCode()) && deviceInfo.getOnlineState().equals(CommonEnum.ENABLE.getCode())) {
                // 将通道全部离线
                deviceInfo.setOnlineState(postDeviceSignInReq.getOnlineState());
                updateDeviceList.add(deviceInfo);
                if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                    updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
                }
                offLineDeviceIdList.add(deviceInfo.getId());
            } else if (isAutoSignIn && postDeviceSignInReq.getOnlineState().equals(CommonEnum.ENABLE.getCode())){
                deviceInfo.setOnlineState(postDeviceSignInReq.getOnlineState());
                updateDeviceList.add(deviceInfo);
                updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
                needChannelSyncDevice.add(deviceInfo.getId());
            }
            detailInfoList.add(getNewDetailInfo(postDeviceSignInReq, nowTime));
        }

        // 新增
        for (PostDeviceSignInReq request : postDeviceSignInReqMap.values()){
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(request.getDeviceId());
            deviceInfo.setGatewayId(request.getGatewayId());
            deviceInfo.setDeviceType(request.getDeviceType());
            deviceInfo.setOnlineState(request.getOnlineState());
            deviceInfo.setCreateTime(nowTime);
            deviceInfo.setUpdateTime(nowTime);
            deviceInfo.setSignState(SignState.TO_BE_ADD.getCode());
            saveDeviceList.add(deviceInfo);
            if (deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
                updateDeviceRedisMap.put(deviceInfo.getId(), deviceInfo.getOnlineState());
            }
            detailInfoList.add(getNewDetailInfo(request, nowTime));
        }

        if (updateDeviceList.size() > 0)
            deviceMapper.batchUpdateOnlineState(updateDeviceList);
        if (saveDeviceList.size() > 0)
            deviceMapper.batchSave(saveDeviceList);
        if (detailInfoList.size() > 0)
            detailBaseService.batchSaveOrUpdate(detailInfoList, DetailType.DEVICE);
        if (updateDeviceRedisMap.size() > 0)
            redisBaseService.batchUpdateDeviceOnlineState(updateDeviceRedisMap);
        if (offLineDeviceIdList.size() > 0)
            channelMapper.batchUpdateOnlineStateByDeviceIds(offLineDeviceIdList, CommonEnum.DISABLE.getCode(), nowTime);
        for (Long deviceId : needChannelSyncDevice){
            Constant.poolExecutor.execute(() -> channelNorthService.channelSync(deviceId));
        }

    }

    private DetailInfo getNewDetailInfo(PostDeviceSignInReq req, LocalDateTime nowTime){
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setDcId(req.getDeviceId());
        detailInfo.setOriginId(req.getOriginId());
        detailInfo.setType(DetailType.DEVICE.getCode());
        detailInfo.setIp(req.getIp());
        detailInfo.setPort(req.getPort());
        detailInfo.setName(req.getName());
        detailInfo.setManufacturer(req.getManufacturer());
        detailInfo.setModel(req.getModel());
        detailInfo.setFirmware(req.getFirmware());
        detailInfo.setUsername(req.getUsername());
        detailInfo.setPassword(req.getPassword());
        detailInfo.setUpdateTime(nowTime);
        return detailInfo;
    }

}
