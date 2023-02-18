package com.runjian.device.expansion.controller.internalPower;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.response.ChannelExpansionFindlistRsp;
import com.runjian.device.expansion.vo.response.PageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjialing
 */
@Api(tags = "对内服务提供")
@Slf4j
@RestController
@RequestMapping("/internalPower")
public class InternalPowerController {
    @Autowired
    IDeviceExpansionService deviceExpansionService;

    @Autowired
    IDeviceChannelExpansionService deviceChannelExpansionService;

    @GetMapping(value = "/videoArea/bindCheck",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安放区域绑定的信息")
    public CommonResponse<Boolean> videoAreaBindCheck(@RequestParam Long areaId) {
        DeviceExpansion oneDeviceExpansion = deviceExpansionService.findOneDeviceByVideoAreaId(areaId);
        DeviceChannelExpansion oneDeviceChannel = deviceChannelExpansionService.findOneDeviceByVideoAreaId(areaId);
        if(!ObjectUtils.isEmpty(oneDeviceExpansion) || !ObjectUtils.isEmpty(oneDeviceChannel)){
            return CommonResponse.success(true);
        }
        return CommonResponse.success(false);
    }
}
