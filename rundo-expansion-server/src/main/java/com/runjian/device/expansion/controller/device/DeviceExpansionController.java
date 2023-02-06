package com.runjian.device.expansion.controller.device;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
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
 * 设备controller
 * @author chenjialing
 */
@Api(tags = "编码器管理")
@Slf4j
@RestController
@RequestMapping("/expansion/device")
public class DeviceExpansionController {
    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private IDeviceExpansionService deviceExpansionService;

    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("添加接口")
    public CommonResponse<Integer> save(@RequestBody DeviceExpansionReq request) {

        validatorService.validateRequest(request);
        return CommonResponse.success(deviceExpansionService.add(request));
    }
}