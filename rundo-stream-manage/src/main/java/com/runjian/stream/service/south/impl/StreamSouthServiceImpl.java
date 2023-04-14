package com.runjian.stream.service.south.impl;

import com.runjian.common.constant.CommonEnum;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.service.common.StreamBaseService;
import com.runjian.stream.service.south.StreamSouthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/7 19:57
 */
@Service
@RequiredArgsConstructor
public class StreamSouthServiceImpl implements StreamSouthService {

    private final StreamMapper streamMapper;

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
