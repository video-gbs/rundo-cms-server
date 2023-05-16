package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.entity.MessageInfo;
import com.runjian.device.service.north.MessageNorthService;
import com.runjian.device.vo.request.PostMessageSubReq;
import com.runjian.device.vo.response.PostMessageSubRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/16 15:13
 */
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageNorthController {

    private final MessageNorthService messageNorthService;

    private final ValidatorService validatorService;

    /**
     * 订阅消息
     * @param req
     * @return
     */
    @PostMapping("/sub")
    public CommonResponse<List<PostMessageSubRsp>> subMsg(PostMessageSubReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(messageNorthService.SubMsg(req.getServiceName(), req.getMsgTypes()));
    }

    /**
     * 取消订阅
     * @param msgHandles 消息句柄
     * @return
     */
    @DeleteMapping("/cancel")
    public CommonResponse<?> cancelMsg(@RequestParam Set<String> msgHandles){
        messageNorthService.cancelSubMsg(msgHandles);
        return CommonResponse.success();
    }

}
