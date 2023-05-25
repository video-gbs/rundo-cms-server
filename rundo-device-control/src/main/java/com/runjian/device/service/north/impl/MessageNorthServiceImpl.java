package com.runjian.device.service.north.impl;

import com.runjian.common.constant.MarkConstant;
import com.runjian.device.constant.SubMsgType;
import com.runjian.device.dao.MessageMapper;
import com.runjian.device.entity.MessageInfo;
import com.runjian.device.service.north.MessageNorthService;
import com.runjian.device.vo.response.PostMessageSubRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Miracle
 * @date 2023/5/16 14:37
 */

@Service
@RequiredArgsConstructor
public class MessageNorthServiceImpl implements MessageNorthService {

    private final MessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PostMessageSubRsp> SubMsg(String serviceName, Set<String> msgTypes) {
        LocalDateTime nowTime = LocalDateTime.now();
        List<MessageInfo> messageInfoList = new ArrayList<>(msgTypes.size());
        List<PostMessageSubRsp> postMessageSubRspList = new ArrayList<>(msgTypes.size());
        for (String msgType : msgTypes){
            SubMsgType subMsgType = SubMsgType.getByName(msgType);
            if (Objects.isNull(subMsgType)){
                continue;
            }
            String msgHandle = MarkConstant.REDIS_DEVICE_CONTROL_MSG + serviceName + MarkConstant.MARK_SPLIT_SEMICOLON + msgType;
            Optional<MessageInfo> messageInfoOp = messageMapper.selectByMsgHandle(msgHandle);
            MessageInfo messageInfo = messageInfoOp.orElseGet(MessageInfo::new);
            if (messageInfoOp.isEmpty()){
                messageInfo.setServiceName(serviceName);
                messageInfo.setMsgType(subMsgType.getCode());
                messageInfo.setCreateTime(nowTime);
                messageInfo.setMsgHandle(msgHandle);
                messageInfo.setMsgLock(MarkConstant.REDIS_DEVICE_CONTROL_LOCK + serviceName + MarkConstant.MARK_SPLIT_SEMICOLON + msgType);
                messageInfoList.add(messageInfo);
            }
            postMessageSubRspList.add(new PostMessageSubRsp(msgType, msgHandle, messageInfo.getMsgLock()));
        }
        messageMapper.batchSave(messageInfoList);
        return postMessageSubRspList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSubMsg(Set<String> msgHandles) {
        if (msgHandles.size() == 0){
            return;
        }
        List<Long> messageInfoIdList = new ArrayList<>(msgHandles.size());
        for (String msgHandle : msgHandles){
            Optional<MessageInfo> messageInfoOp = messageMapper.selectByMsgHandle(msgHandle);
            if (messageInfoOp.isEmpty()){
                continue;
            }
            messageInfoIdList.add(messageInfoOp.get().getId());
        }
        messageMapper.batchDelete(messageInfoIdList);
    }
}
