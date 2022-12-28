package com.runjian.device.service.north.impl;

import com.runjian.device.service.north.PlayVideoNorthService;
import com.runjian.device.vo.response.VideoPlayRsp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayVideoNorthServiceImpl implements PlayVideoNorthService {
    @Override
    public VideoPlayRsp play(Long chId) {
        // todo 将chId通过feign
        return null;
    }

    @Override
    public VideoPlayRsp playBack(Long chId, LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    @Override
    public void stopPlay(Long chId) {

    }
}
