package com.runjian.device.expansion.controller.channel;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.vo.request.*;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备controller
 * @author chenjialing
 */
@Api(tags = "通道管理")
@Slf4j
@RestController
//@CrossOrigin
@RequestMapping("/expansion/channel")
public class DeviceChannelExpansionController {
    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private IDeviceChannelExpansionService deviceChannelExpansionService;

    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("添加接口")
    public CommonResponse<Boolean> save(@RequestBody FindChannelListReq findChannelListReq) {

        validatorService.validateRequest(findChannelListReq);
        return deviceChannelExpansionService.add(findChannelListReq);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("编辑")
    public CommonResponse<Boolean> edit(@RequestBody DeviceChannelExpansionReq request) {

        validatorService.validateRequest(request);
        return deviceChannelExpansionService.edit(request);
    }

    @DeleteMapping(value = "/delete",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("删除")
    public CommonResponse<Long> delete(@RequestParam Long id) {

        return deviceChannelExpansionService.remove(id);
    }

    @DeleteMapping(value = "/batchDelete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("批量删除")
    public CommonResponse<Boolean> batchDelete(@RequestBody List<Long> idList) {
       return deviceChannelExpansionService.removeBatch(idList);
    }

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("列表")
    public CommonResponse<PageResp<DeviceChannelExpansionResp>> list(@RequestBody DeviceChannelExpansionListReq request) {

        return CommonResponse.success(deviceChannelExpansionService.list(request));
    }

    @PostMapping(value = "/findList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("发现通道列表")
    public CommonResponse<PageResp<DeviceChannelExpansionResp>> findList() {

        return CommonResponse.success(deviceChannelExpansionService.findList());
    }

    @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("移动")
    public CommonResponse<Boolean> move(@RequestBody MoveReq moveReq) {

        return CommonResponse.success(deviceChannelExpansionService.move(moveReq));
    }


}
