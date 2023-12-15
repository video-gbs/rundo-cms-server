package com.runjian.device.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.north.DeviceNorthService;
import com.runjian.device.vo.request.PostDeviceAddReq;
import com.runjian.device.vo.request.PutDeviceSignSuccessReq;
import com.runjian.device.vo.request.PutPlatformSubscribeReq;
import com.runjian.device.vo.response.DeviceSyncRsp;
import com.runjian.device.vo.response.GetDevicePageRsp;
import com.runjian.device.vo.response.GetNodeRsp;
import com.runjian.device.vo.response.PostDeviceAddRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/1/9 15:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/device/north")
public class DeviceNorthController {

    private final DeviceNorthService deviceNorthService;

    private final ValidatorService validatorService;

    /**
     * 设备分页获取
     * @param page 页码
     * @param num 每页数据量
     * @param signState 注册状态
     * @param deviceName 设备名称
     * @param ip ip地址
     * @return
     */
    @GetMapping("/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetDevicePageRsp>> getDeviceByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, Integer signState, String deviceName, String ip){
        return CommonResponse.success(deviceNorthService.getDeviceByPage(page, num, signState, deviceName, ip));
    }

    /**
     * 获取下级平台分页数据
     * @param page 页码
     * @param num 每页数据量
     * @param deviceName 设备名称
     * @param ip ip地址
     * @return
     */
    @GetMapping("/platform/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetDevicePageRsp>> getPlatformByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, String deviceName, String ip){
        return CommonResponse.success(deviceNorthService.getPlatformByPage(page, num, deviceName, ip));
    }

    /**
     * 获取节点数据
     * @param deviceId 设备id
     * @return
     */
    @GetMapping("/node/data")
    public CommonResponse<List<GetNodeRsp>> getNodeRsp(@RequestParam Long deviceId){
        return CommonResponse.success(deviceNorthService.getNodeRsp(deviceId));
    }

    /**
     * 设备添加
     * @param req 设备添加请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<PostDeviceAddRsp> deviceAdd(@RequestBody PostDeviceAddReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(deviceNorthService.deviceAdd(req.getDeviceId(), req.getGatewayId(), req.getDeviceType(), req.getIp(), req.getPort(), req.getName(), req.getManufacturer(), req.getModel(), req.getFirmware(), req.getPtzType(), req.getUsername(), req.getPassword()));
    }


    /**
     * 设备同步
     * @param deviceId 设备id
     * @return
     */
    @GetMapping("/sync")
    public CommonResponse<DeviceSyncRsp> deviceSync(@RequestParam Long deviceId){
        return CommonResponse.success(deviceNorthService.deviceSync(deviceId));
    }

    /**
     * 设备注册成功状态转变
     * @param request 设备注册状态转为成功请求体
     * @return
     */
    @PutMapping("/sign/success")
    public CommonResponse<?> deviceSignSuccess(@RequestBody PutDeviceSignSuccessReq request){
        validatorService.validateRequest(request);
        deviceNorthService.deviceSignSuccess(request.getDeviceId());
        return CommonResponse.success();
    }

    /**
     * 删除设备
     * @param deviceId 设备id
     * @return
     */
    @DeleteMapping("/delete/soft")
    public CommonResponse<?> deviceDeleteSoft(@RequestParam Long deviceId){
        deviceNorthService.deviceDeleteSoft(deviceId);
        return CommonResponse.success();
    }

    /**
     * 删除设备
     * @param deviceId 设备id
     * @return
     */
    @DeleteMapping("/delete/hard")
    public CommonResponse<?> deviceDeleteHard(@RequestParam Long deviceId){
        deviceNorthService.deviceDeleteHard(deviceId);
        return CommonResponse.success();
    }

    /**
     * 下级平台订阅
     * @param request
     * @return
     */
    @PutMapping("/platform/subscribe")
    public CommonResponse<?> platformSubscribe(@RequestBody PutPlatformSubscribeReq request){
        validatorService.validateRequest(request);
        deviceNorthService.deviceSubscribe(request.getDeviceId(), request.getIsSubscribe());
        return CommonResponse.success();
    }

}
