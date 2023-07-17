package com.runjian.device.expansion.controller.device;

import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.aspect.annotation.DeviceStatusPoint;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.feign.response.DeviceAddResp;
import com.runjian.device.expansion.vo.request.*;
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
@Api(tags = "编码器管理")
@Slf4j
@RestController
//@CrossOrigin
@RequestMapping("/expansion/device")
public class DeviceExpansionController {
    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private IDeviceExpansionService deviceExpansionService;

    @PostMapping(value = "/add",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("添加接口")
    public CommonResponse<DeviceAddResp> save(@RequestBody DeviceExpansionReq request) {

        validatorService.validateRequest(request);
        return deviceExpansionService.add(request);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("编辑")
    public CommonResponse<Long> edit(@RequestBody DeviceExpansionEditReq request) {

        validatorService.validateRequest(request);
        return deviceExpansionService.edit(request);
    }

    @DeleteMapping(value = "/delete",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("删除")
    public CommonResponse<Long> delete(@RequestParam Long id) {

        return deviceExpansionService.remove(id);
    }

    @PostMapping(value = "/batchDelete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("批量删除")
    public CommonResponse<Boolean> batchDelete(@RequestBody List<Long> idList) {
       return deviceExpansionService.removeBatch(idList);
    }

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("列表")
    @DeviceStatusPoint
    public CommonResponse<PageResp<DeviceExpansionResp>> list(@RequestBody DeviceExpansionListReq deviceExpansionListReq) {

        return CommonResponse.success(deviceExpansionService.list(deviceExpansionListReq));
    }

    @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("移动")
    public CommonResponse<Boolean> move(@RequestBody MoveReq deviceExpansionMoveReq) {

        return CommonResponse.success(deviceExpansionService.move(deviceExpansionMoveReq));
    }


    /**
     * 设备分页获取
     * @param page 页码
     * @param num 每页数据量
     * @param signState 注册状态
     * @param deviceName 设备名称
     * @param ip ip地址
     * @return
     */
    @ApiOperation("待注册列表")
    @GetMapping("/unregister/list")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<Object> getDeviceByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, Integer signState, String deviceName, String ip){

        return CommonResponse.success(deviceExpansionService.getDeviceByPage(page, num, signState, deviceName, ip));


    }


}
