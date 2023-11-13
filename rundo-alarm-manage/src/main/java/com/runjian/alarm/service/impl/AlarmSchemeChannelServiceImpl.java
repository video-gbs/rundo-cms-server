package com.runjian.alarm.service.impl;

import cn.hutool.log.Log;
import com.alibaba.fastjson2.JSONObject;
import com.github.javaparser.utils.StringEscapeUtils;
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
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.SignState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    private final RedissonClient redissonClient;

    private Map<String, PostMessageSubRsp> messageSubRspMap = new HashMap<>();

    private final AlarmSchemeChannelRelMapper alarmSchemeChannelRelMapper;

    private final AlarmSchemeService alarmSchemeService;

    @Override
    public void init() {
        PostMessageSubReq postMessageSubReq = new PostMessageSubReq("alarm-manage", Set.of("channelAddOrDelete"));
        CommonResponse<List<PostMessageSubRsp>> commonResponse = deviceControlApi.subMsg(postMessageSubReq);
        if (commonResponse.isError()){
            log.error(LogTemplate.ERROR_LOG_TEMPLATE, "设备控制接口调用服务", "订阅消息失败", postMessageSubReq);
            return;
        }
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
        RLock rLock = redissonClient.getLock(channelAddOrDelete.getMsgLock());
        if (rLock.tryLock()){
            try{
                Map<Object, Object> entries = redisTemplate.opsForHash().entries(channelAddOrDelete.getMsgHandle());
                if (entries.isEmpty()){
                    return;
                }
                Set<Long> deleteChannelIds = new HashSet<>();
                log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "预案通道处理服务", "接收到添加或删除通道消息", entries);
                for (Map.Entry<Object, Object> entry : entries.entrySet()){
                    String jsonOb = StringEscapeUtils.unescapeJava(entry.getValue().toString());
                    log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "预案通道处理服务", "异常消息解读", jsonOb);
                    JSONObject jsonObject = JSONObject.parseObject(jsonOb);
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
            } finally {
                rLock.unlock();
            }
        }
    }
}
