package com.runjian.device.expansion.controller.ptz;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.service.IChannelPresetService;
import com.runjian.device.expansion.service.IPtzService;
import com.runjian.device.expansion.vo.request.ChannelPresetControlReq;
import com.runjian.device.expansion.vo.request.ChannelPresetEditReq;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionReq;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    IPtzService ptzService;

    @Autowired
    IChannelPresetService channelPresetService;


    @PutMapping(value = "/operation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("ptz相关的指令")
    public CommonResponse<?> edit(@RequestBody ChannelPtzControlReq request) {

        validatorService.validateRequest(request);
        return ptzService.ptzOperation(request);
    }

    @GetMapping(value = "/preset/select", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位查询:channelExpansionId为通道id")
    public CommonResponse<List<ChannelPresetListsResp>> presetSelect(@RequestParam Long channelExpansionId) {

        return channelPresetService.presetSelect(channelExpansionId);
    }

    @PutMapping(value = "/preset/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位编辑:channelExpansionId为通道id")
    public CommonResponse<Boolean> presetEdit(@RequestBody ChannelPresetEditReq channelPresetEditReq) {
        return  channelPresetService.presetEdit(channelPresetEditReq);
    }

    @DeleteMapping(value = "/preset/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位删除:channelExpansionId为通道id")
    public CommonResponse<Boolean> presetDelete(@RequestBody ChannelPresetControlReq channelPresetControlReq) {

        return channelPresetService.presetDelete(channelPresetControlReq);
    }

    @PutMapping(value = "/preset/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("预置位执行:channelExpansionId为通道id")
    public CommonResponse<Boolean> presetInvoke(@RequestBody ChannelPresetControlReq channelPresetControlReq) {

        return channelPresetService.presetInvoke(channelPresetControlReq);
    }
}
