package com.runjian.alarm.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.alarm.dao.relation.AlarmSchemeChannelRelMapper;
import com.runjian.alarm.entity.relation.AlarmSchemeChannelRel;
import com.runjian.alarm.feign.DeviceControlApi;
import com.runjian.alarm.service.AlarmSchemeChannelService;
import com.runjian.alarm.service.AlarmSchemeService;
import com.runjian.alarm.utils.RedisLockUtil;
import com.runjian.alarm.vo.feign.PostMessageSubReq;
import com.runjian.alarm.vo.feign.PostMessageSubRsp;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.SignState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/11/8 11:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmSchemeChannelServiceImpl implements AlarmSchemeChannelService {

    private final DeviceControlApi deviceControlApi;

    private final RedisLockUtil redisLockUtil;

    private final StringRedisTemplate redisTemplate;

    private Map<String, PostMessageSubRsp> messageSubRspMap;

    private final AlarmSchemeChannelRelMapper alarmSchemeChannelRelMapper;

    private final AlarmSchemeService alarmSchemeService;

    @Override
    public void init() {
        CommonResponse<List<PostMessageSubRsp>> commonResponse = deviceControlApi.subMsg(new PostMessageSubReq("alarm-manage", Set.of("channelAddOrDelete")));
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        messageSubRspMap = commonResponse.getData().stream().collect(Collectors.toMap(PostMessageSubRsp::getMsgType, postMessageSubRsp -> postMessageSubRsp));
    }

    @Override
    @Scheduled(fixedDelay = 3000)
    public void checkDeleteChannel() {
        PostMessageSubRsp channelAddOrDelete = messageSubRspMap.get("channelAddOrDelete");
        if(Objects.isNull(channelAddOrDelete)){
            init();
            return;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        if (redisLockUtil.lock(channelAddOrDelete.getMsgLock(), nowTime.toString(), 5, TimeUnit.SECONDS, 1)){
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(channelAddOrDelete.getMsgHandle());
            Set<Long> deleteChannelIds = new HashSet<>();
            for (Map.Entry<Object, Object> entry : entries.entrySet()){
                JSONObject jsonObject = JSONObject.parseObject(entry.getValue().toString());
                Integer signState = jsonObject.getInteger("signState");
                if (Objects.equals(signState, SignState.DELETED.getCode())){
                    deleteChannelIds.add(Long.parseLong(entry.getKey().toString()));
                }
            }
            List<AlarmSchemeChannelRel> alarmSchemeChannelRelList = alarmSchemeChannelRelMapper.selectByChannelIds(deleteChannelIds);
            if (!alarmSchemeChannelRelList.isEmpty()){
                alarmSchemeChannelRelMapper.batchDelete(alarmSchemeChannelRelList.stream().map(AlarmSchemeChannelRel::getId).collect(Collectors.toList()));
                alarmSchemeService.defense(alarmSchemeChannelRelList.stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList()), false);
            }
        }
    }
}
