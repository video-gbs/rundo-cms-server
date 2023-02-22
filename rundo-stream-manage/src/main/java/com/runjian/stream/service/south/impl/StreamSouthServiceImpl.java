package com.runjian.stream.service.south.impl;

import com.runjian.common.constant.CommonEnum;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.service.common.StreamBaseService;
import com.runjian.stream.service.south.StreamSouthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/7 19:57
 */
@Service
public class StreamSouthServiceImpl implements StreamSouthService {

    @Autowired
    private StreamMapper streamMapper;


    @Override
    public void receiveResult(String streamId, Boolean isSuccess) {
        Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
        StreamBaseService.STREAM_OUT_TIME_ARRAY.deleteTime(streamId);
        if (streamInfoOp.isEmpty()) {
            return;
        }
        if (isSuccess) {
            StreamInfo streamInfo = streamInfoOp.get();
            streamInfo.setStreamState(CommonEnum.ENABLE.getCode());
            streamInfo.setUpdateTime(LocalDateTime.now());
            streamMapper.updateStreamState(streamInfo);
        } else {
            streamMapper.deleteByStreamId(streamId);
        }
    }

    @Override
    public Boolean streamCloseHandle(String streamId, Boolean canClose) {
        Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
        if (streamInfoOp.isEmpty() || !canClose || streamInfoOp.get().getAutoCloseState().equals(CommonEnum.ENABLE.getCode())) {
            streamMapper.deleteByStreamId(streamId);
            return true;
        }
        return false;
    }
}
