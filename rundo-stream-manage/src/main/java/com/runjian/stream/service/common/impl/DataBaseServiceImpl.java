package com.runjian.stream.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.stream.dao.DispatchMapper;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.service.common.DataBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/3 15:00
 */
@Service
@RequiredArgsConstructor
public class DataBaseServiceImpl implements DataBaseService {

    private final DispatchMapper dispatchMapper;

    private final StreamMapper streamMapper;

    @Override
    public DispatchInfo getDispatchInfo(Long dispatchId) {
        Optional<DispatchInfo> dispatchInfoOp = dispatchMapper.selectById(dispatchId);
        if (dispatchInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("调度服务%s不存在", dispatchId));
        }
        return dispatchInfoOp.get();
    }

    @Override
    public DispatchInfo getOnlineDispatchInfo(Long dispatchId) {
        DispatchInfo dispatchInfo = getDispatchInfo(dispatchId);
        if (dispatchInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("调度服务'%s'已掉线", dispatchInfo.getName()));
        }
        return dispatchInfo;
    }

    @Override
    public StreamInfo getStreamInfoByStreamId(String streamId){
        Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
        if (streamInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("流%s不存在", streamId));
        }
        return streamInfoOp.get();
    }
}
