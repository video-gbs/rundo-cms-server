package com.runjian.device.expansion.controller.ptz;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.vo.request.ChannelPresetControlReq;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionReq;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/preset/select", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位查询:channelExpansionId为通道id")
    public CommonResponse<ChannelPresetListsResp> presetSelect(@RequestParam Long channelExpansionId) {

        return null;
    }

    @PutMapping(value = "/preset/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位编辑:channelExpansionId为通道id")
    public CommonResponse<Boolean> presetEdit(@RequestParam Long channelExpansionId) {

        return null;
    }

    @DeleteMapping(value = "/preset/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位删除:channelExpansionId为通道id")
    public CommonResponse<Boolean> presetDelete(@RequestParam Long channelExpansionId) {

        return null;
    }

    @PutMapping(value = "/preset/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位执行:channelExpansionId为通道id")
    public CommonResponse<Boolean> presetInvoke(@RequestBody ChannelPresetControlReq channelPresetControlReq) {

        return null;
    }
}
