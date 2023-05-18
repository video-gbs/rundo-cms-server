package com.runjian.device.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.common.constant.MsgType;
import com.runjian.device.constant.SubMsgType;
import com.runjian.device.dao.MessageMapper;
import com.runjian.device.entity.MessageInfo;
import com.runjian.device.service.common.MessageBaseService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Miracle
 * @date 2023/2/23 11:07
 */
@Service
@RequiredArgsConstructor
public class MessageBaseServiceImpl implements MessageBaseService {

    private final RedissonClient redissonClient;

    private final MessageMapper messageMapper;

    /**
     * 消息分发
     * @param subMsgType
     * @param data
     */
    public void msgDistribute(SubMsgType subMsgType, Map<Long, Object> data){
        List<MessageInfo> messageInfoList = messageMapper.selectAllByMsgType(subMsgType.getCode());
        for (MessageInfo messageInfo : messageInfoList){
            redissonClient.getMap(messageInfo.getMsgHandle()).putAll(data);
        }
    }

    @Override
    public boolean checkMsgConsumeFinish(SubMsgType subMsgType, Set<Object> ids) {
        List<MessageInfo> messageInfoList = messageMapper.selectAllByMsgType(subMsgType.getCode());
        for (MessageInfo messageInfo : messageInfoList){
            if (redissonClient.getMap(messageInfo.getMsgHandle()).getAll(ids).size() > 0){
                return false;
            }
        }
        return true;
    }

}
