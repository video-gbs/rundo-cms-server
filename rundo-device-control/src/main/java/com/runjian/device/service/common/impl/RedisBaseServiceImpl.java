package com.runjian.device.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.service.common.RedisBaseService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Miracle
 * @date 2023/2/23 11:07
 */
@Service
public class RedisBaseServiceImpl implements RedisBaseService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void batchUpdateChannelOnlineState(Map<Long, Integer> channelOnlineStateMap) {
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_CHANNEL_ONLINE_STATE_LOCK);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            redissonClient.getMap(MarkConstant.REDIS_CHANNEL_ONLINE_STATE).putAll(channelOnlineStateMap);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void batchUpdateDeviceOnlineState(Map<Long, Integer> deviceOnlineStateMap) {
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_DEVICE_ONLINE_STATE_LOCK);
        try{
            lock.lock(3, TimeUnit.SECONDS);
            redissonClient.getMap(MarkConstant.REDIS_DEVICE_ONLINE_STATE).putAll(deviceOnlineStateMap);
        } catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateDeviceOnlineState(Long deviceId,  Integer onlineState) {
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_DEVICE_ONLINE_STATE_LOCK);
        try{
            lock.lock(3, TimeUnit.SECONDS);
            redissonClient.getMap(MarkConstant.REDIS_DEVICE_ONLINE_STATE).put(deviceId, onlineState);
        } catch (Exception ex){
            ex.printStackTrace();
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
