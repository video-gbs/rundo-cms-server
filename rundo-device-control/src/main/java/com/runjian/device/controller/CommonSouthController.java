package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.vo.feign.DeviceControlReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Miracle
 * @date 2023/1/30 11:09
 */
@Slf4j
@RestController
@RequestMapping("/common/south")
public class CommonSouthController {

    @Autowired
    private ValidatorService validatorService;

    @PostMapping("/event")
    public CommonResponse<?> event(@RequestBody DeviceControlReq req){
        validatorService.validateRequest(req);
        log.info(req.toString());
        return CommonResponse.success();
    }
}
