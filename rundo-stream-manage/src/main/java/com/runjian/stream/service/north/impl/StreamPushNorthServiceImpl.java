package com.runjian.stream.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;
import com.runjian.stream.constant.StreamPushState;
import com.runjian.stream.dao.StreamPushMapper;
import com.runjian.stream.entity.StreamPushInfo;
import com.runjian.stream.service.north.StreamNorthService;
import com.runjian.stream.service.north.StreamPushNorthService;
import com.runjian.stream.vo.response.PostStreamPushInitRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/12/7 10:33
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StreamPushNorthServiceImpl implements StreamPushNorthService {

    private final StreamPushMapper streamPushMapper;

    private final RedissonClient redissonClient;

    private final StreamNorthService streamNorthService;

    @PostConstruct
    public void initSrcPort(){
        List<Integer> streamPushInfoList = streamPushMapper.selectAll().stream().map(StreamPushInfo::getSrcPort).collect(Collectors.toList());

        redissonClient.getQueue(MarkConstant.REDIS_STREAM_PUSH_SRC_PORT_QUEUE).
    }

    @Override
    public PostStreamPushInitRsp streamPushInit(Long channelId, String ssrc, String dstUrl, Integer dstPort, Integer transferMode, LocalDateTime startTime, LocalDateTime endTime) {
        StreamPushInfo streamPushInfo = new StreamPushInfo();
        LocalDateTime nowTime = LocalDateTime.now();
        streamPushInfo.setChannelId(channelId);
        streamPushInfo.setSsrc(ssrc);
        streamPushInfo.setDstUrl(dstUrl);
        streamPushInfo.setDstPort(dstPort);
        streamPushInfo.setSrcPort(srcPort);
        streamPushInfo.setTransferMode(transferMode);
        streamPushInfo.setState(CommonEnum.DISABLE.getCode());
        streamPushInfo.setStartTime(startTime);
        streamPushInfo.setEndTime(endTime);
        streamPushInfo.setCreateTime(nowTime);
        streamPushInfo.setUpdateTime(nowTime);
        return srcPort;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void streamPush(Long streamPushId) {
        Optional<StreamPushInfo> streamPushInfoOp = streamPushMapper.selectById(streamPushId);
        if (streamPushInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("未找到 %s 对应流推信息,可能已过期,请重新申请端口", streamPushId));
        }
        StreamPushInfo streamPushInfo = streamPushInfoOp.get();
        String streamId = streamNorthService.streamPushPlay(streamPushInfo.getChannelId(), streamPushInfo.getDstUrl(), streamPushInfo.getSsrc(), streamPushInfo.getDstPort(), streamPushInfo.getSrcPort(), streamPushInfo.getTransferMode(), streamPushInfo.getStartTime(), streamPushInfo.getEndTime());
        streamPushInfo.setStreamId(streamId);
        streamPushInfo.setUpdateTime(LocalDateTime.now());
        streamPushInfo.setState(CommonEnum.ENABLE.getCode());
        streamPushMapper.update(streamPushInfo);
    }

    @Override
    public void streamPushStop(Long streamPushId) {
        Optional<StreamPushInfo> streamPushInfoOp = streamPushMapper.selectById(streamPushId);
        if (streamPushInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("未找到 %s 对应流推信息", streamPushId));
        }
        StreamPushInfo streamPushInfo = streamPushInfoOp.get();
        if (streamNorthService.stopPush(streamPushInfo.getStreamId(), streamPushInfo.getSsrc())){
            streamPushMapper.deleteById(streamPushId);
            return;
        }
        streamPushInfo.setState(StreamPushState.DELETE.getCode());
        streamPushInfo.setUpdateTime(LocalDateTime.now());
        streamPushMapper.update(streamPushInfo);
    }

    @Override
    @Scheduled(fixedDelay = 10000)
    public void checkStreamPushState() {
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_STREAM_PUSH_STATE_CHECK_LOCK);
        if (lock.tryLock()){
            try{
                streamPushMapper.selectByState(StreamPushState.DELETE.getCode()).forEach(streamPushInfo -> {
                    if (streamNorthService.stopPush(streamPushInfo.getStreamId(), streamPushInfo.getSsrc())){
                        streamPushMapper.deleteById(streamPushInfo.getId());
                    }
                });

                LocalDateTime nowTime = LocalDateTime.now();
                streamPushMapper.selectByState(StreamPushState.INIT.getCode()).forEach(streamPushInfo -> {
                    if(streamPushInfo.getUpdateTime().isBefore(nowTime.plusSeconds(-15))){
                        streamPushMapper.deleteById(streamPushInfo.getId());
                    }
                });
            } finally {
                lock.unlock();
            }
        }
    }


}
