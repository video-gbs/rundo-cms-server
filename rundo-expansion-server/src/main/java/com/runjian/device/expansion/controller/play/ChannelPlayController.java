package com.runjian.device.expansion.controller.play;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.service.IPlayService;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
import com.runjian.device.expansion.vo.request.PlayBackReq;
import com.runjian.device.expansion.vo.request.PlayReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjialing
 */
@Api(tags = "流播放操作")
@Slf4j
@RestController
@RequestMapping("/expansion/play")
public class ChannelPlayController {
    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private IPlayService playService;

    @PostMapping(value = "/live",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("直播接口")
    public CommonResponse<StreamInfo>  live(@RequestBody PlayReq request) {
        validatorService.validateRequest(request);

        return playService.play(request);
    }

    @PostMapping(value = "/back",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("回放接口")
    public CommonResponse<StreamInfo>  back(@RequestBody PlayBackReq request) {
        validatorService.validateRequest(request);

        return playService.playBack(request);
    }
}
