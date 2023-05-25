package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.south.StreamSouthService;
import com.runjian.stream.vo.request.PutStreamCloseReq;
import com.runjian.stream.vo.request.PutStreamReceiveResultReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/2/7 20:18
 */
@RestController
@RequestMapping("/stream/south")
@RequiredArgsConstructor
public class StreamSouthController {

    private final StreamSouthService streamSouthService;

    private final ValidatorService validatorService;

    /**
     * 自动关闭
     * @param req 流id
     * @return 是否关闭
     */
    @PutMapping("/play/close")
    public CommonResponse<Boolean> streamCloseHandle(@RequestBody PutStreamCloseReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamSouthService.streamCloseHandle(req.getStreamId(), req.getCanClose()));
    }


}
