package com.runjian.device.expansion.controller.ptz;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjialing
 */
@Api(tags = "ptz操作")
@Slf4j
@RestController
@RequestMapping("/expansion/ptz")
public class PtzController {
    @Autowired
    private ValidatorService validatorService;

    @PutMapping(value = "/operation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("ptz相关的指令")
    public CommonResponse<Boolean> edit(@RequestBody ChannelPtzControlReq request) {

        validatorService.validateRequest(request);
        return null;
    }

}
