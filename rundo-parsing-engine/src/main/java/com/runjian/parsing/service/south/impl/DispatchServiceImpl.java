package com.runjian.parsing.service.south.impl;

import com.alibaba.druid.util.StringUtils;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.constant.SignType;
import com.runjian.parsing.dao.DispatchMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.feign.StreamManageApi;
import com.runjian.parsing.service.south.DispatchService;
import com.runjian.parsing.vo.request.PostDispatchSignInReq;
import com.runjian.parsing.vo.request.PutDispatchHeartbeatReq;
import com.runjian.parsing.vo.response.SignInRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/7 18:05
 */
@Slf4j
@Service
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private StreamManageApi streamManageApi;

    @Override
    public SignInRsp signIn(String serialNum, Integer signType, String ip, String port, String outTime) {
        Optional<DispatchInfo> dispatchInfoOp = dispatchMapper.selectBySerialNum(serialNum);
        SignInRsp signInRsp = new SignInRsp();
        DispatchInfo dispatchInfo = dispatchInfoOp.orElse(new DispatchInfo());
        if (dispatchInfoOp.isEmpty()){
            LocalDateTime nowTime = LocalDateTime.now();
            dispatchInfo.setSerialNum(serialNum);
            dispatchInfo.setSignType(signType);
            dispatchInfo.setIp(ip);
            dispatchInfo.setPort(port);
            dispatchInfo.setCreateTime(nowTime);
            dispatchInfo.setUpdateTime(nowTime);
            dispatchMapper.save(dispatchInfo);
            signInRsp.setIsFirstSignIn(true);
        } else {
            signInRsp.setIsFirstSignIn(false);
        }
        signInRsp.setDispatchId(dispatchInfo.getId());
        PostDispatchSignInReq req = new PostDispatchSignInReq(dispatchInfo, Instant.ofEpochMilli(Long.parseLong(outTime)).atZone(ZoneId.systemDefault()).toLocalDateTime());
        CommonResponse<?> response = streamManageApi.dispatchSignIn(req);
        if (response.getCode() == 0){
            log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "流媒体服务注册服务", "流媒体服注册成功", dispatchInfo.getId());
        }
        signInRsp.setSignType(SignType.MQ.getMsg());
        return signInRsp;
    }

    @Override
    public Long heartbeat(String serialNum, String heartbeatTime) {
        if (!StringUtils.isNumber(heartbeatTime) || System.currentTimeMillis() >= Long.parseLong(heartbeatTime)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("非法时间戳:%s", heartbeatTime));
        }
        Optional<DispatchInfo> dispatchInfoOp = dispatchMapper.selectBySerialNum(serialNum);
        if (dispatchInfoOp.isEmpty()){
            return null;
        }
        PutDispatchHeartbeatReq putDispatchHeartbeatReq = new PutDispatchHeartbeatReq();
        putDispatchHeartbeatReq.setOutTime(Instant.ofEpochMilli(Long.parseLong(heartbeatTime)).atZone(ZoneId.systemDefault()).toLocalDateTime());
        putDispatchHeartbeatReq.setDispatchId(dispatchInfoOp.get().getId());
        CommonResponse<Boolean> response = streamManageApi.dispatchHeartbeat(putDispatchHeartbeatReq);
        if (response.getCode() != 0){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        if (!response.getData()){
            return null;
        }
        return dispatchInfoOp.get().getId();
    }
}
