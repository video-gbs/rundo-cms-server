package com.runjian.device.expansion.controller.channel;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.aspect.annotation.ChannelStatusPoint;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.vo.feign.response.ChannelSyncRsp;
import com.runjian.device.expansion.vo.feign.response.VideoRecordRsp;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionListReq;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionReq;
import com.runjian.device.expansion.vo.request.FindChannelListReq;
import com.runjian.device.expansion.vo.request.MoveReq;
import com.runjian.device.expansion.vo.response.ChannelExpansionFindlistRsp;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionPlayResp;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 设备controller
 * @author chenjialing
 */
@Api(tags = "通道管理")
@Slf4j
@RestController
//@CrossOrigin
@RequestMapping("/expansion/channel/internal")
public class DeviceChannelExpansionInternalController {
    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private IDeviceChannelExpansionService deviceChannelExpansionService;




    @GetMapping(value = "/getListBatch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("批量--通道列表")
    @ChannelStatusPoint
    public CommonResponse<List<DeviceChannelExpansion>> getListBatch(@RequestParam  List<Long> channelIds) {

        return CommonResponse.success(deviceChannelExpansionService.batchChannelList(channelIds));
    }


}
