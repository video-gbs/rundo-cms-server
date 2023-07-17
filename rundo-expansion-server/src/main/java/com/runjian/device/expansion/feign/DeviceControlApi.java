package com.runjian.device.expansion.feign;

import cn.hutool.json.JSONObject;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.DeviceControlApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.*;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import com.runjian.device.expansion.vo.response.DeviceUnRegisterPageRsp;
import com.runjian.device.expansion.vo.response.PageInfo;
import com.runjian.device.expansion.vo.response.PageResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "device-control",fallbackFactory= DeviceControlApiFallbackFactory.class)
public interface DeviceControlApi {


    /************************************************设备-start**********************************************************/
    /**
     * 控制服务 设备添加
     * @param deviceReq
     * @return
     */
    @PostMapping(value = "/device/north/add",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<DeviceAddResp> deviceAdd(@RequestBody DeviceReq deviceReq);

    /**
     * 控制服务 设备删除
     * @param deviceId
     * @return
     */
    @DeleteMapping("/device/north/delete/soft")
    CommonResponse deleteDeviceSoft(@RequestParam Long deviceId);


    /**
     * 控制服务 设备删除
     * @param deviceId
     * @return
     */
    @DeleteMapping("/device/north/delete/hard")
    CommonResponse deleteDeviceHard(@RequestParam Long deviceId);
    /**
     * 设备注册状态修改
     * @param putDeviceSignSuccessReq
     * @return
     */
    @PutMapping("/device/north/sign/success")
    CommonResponse deviceSignSuccess(@RequestBody PutDeviceSignSuccessReq putDeviceSignSuccessReq);

    /**
     * 设备分页获取
     * @param page 页码
     * @param num 每页数据量
     * @param signState 注册状态
     * @param deviceName 设备名称
     * @param ip ip地址
     * @return
     */
    @GetMapping("/device/north/page")
    CommonResponse<PageInfo<DeviceUnRegisterPageRsp>> getDeviceByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, Integer signState, String deviceName, String ip);



    /************************************************通道-start**********************************************************/
    /**
     * 控制服务 通道添加状态修改
     * @param putChannelSignSuccessReq
     * @return
     */
    @PutMapping(value = "/channel/north/sign/success",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<Boolean> channelSignSuccess(@RequestBody PutChannelSignSuccessReq putChannelSignSuccessReq);


    /**
     * 控制服务 通道待添加列表
     * @param page
     * @param num
     * @param nameOrOriginId
     * @return
     */
    @GetMapping(value = "/channel/north/page")
    CommonResponse<PageListResp<GetChannelByPageRsp>> getChannelByPage(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10") int num, @RequestParam(required = false) String nameOrOriginId);


    /**
     * 控制服务 通道同步
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/channel/north/sync")
    CommonResponse<ChannelSyncRsp> channelSync(@RequestParam Long deviceId);

    /**
     * 控制服务 通道删除
     * @param channelIds
     * @return
     */
    @DeleteMapping(value = "/channel/north/delete")
    CommonResponse<Boolean> channelDelete(@RequestParam List<Long> channelIds);


    /**
     * 控制服务 通道删除
     * @param channelId
     * @return
     */
    @DeleteMapping(value = "/channel/north/delete/soft")
    CommonResponse<Boolean> channelDeleteSoft(@RequestParam Long channelId);

    /**
     * 控制服务 通道删除
     * @param channelIds
     * @return
     */
    @DeleteMapping(value = "/channel/north/delete/hard")
    CommonResponse<Boolean> channelDeleteHard(@RequestParam Long channelIds);
    /**
     * 点播接口
     * @param playFeignReq
     * @return
     */
    @Deprecated
    @PostMapping(value = "/channel/north/play",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> play(@RequestBody PlayFeignReq playFeignReq);

    /**
     * 回放接口
     * @param playBackReq
     * @return
     */
    @PostMapping(value = "/channel/north/playback",produces = MediaType.APPLICATION_JSON_VALUE)
    @Deprecated
    CommonResponse<StreamInfo> playBack(@RequestBody PlayBackFeignReq playBackReq);


    /**
     * 云台控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/channel/north/ptz/control")
    CommonResponse<?> ptzControl(@RequestBody FeignPtzControlReq req);

    /**
     * 获取预置位
     * @param channelId 通道id
     * @return
     */
    @GetMapping("/channel/north/ptz/preset")
    CommonResponse<List<ChannelPresetListsResp>> getPtzPreset(@RequestParam Long channelId);

    /**
     * 3d放大放小控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/channel/north/ptz/3d")
    CommonResponse<?> ptz3d(@RequestBody FeignPtz3dReq req);

    /**
     * 视频回放
     * @param channelId 回放请求体
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 视频播放返回体
     */
    @GetMapping("/channel/north/record")
    CommonResponse<VideoRecordRsp> videoRecordInfo(@RequestParam Long channelId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime);


    /**
     * 订阅消息
     * @param req
     * @return
     */
    @PostMapping("/message/sub")
    public CommonResponse<List<MessageSubRsp>> subMsg(@RequestBody MessageSubReq req);
    /**
     * 取消订阅
     * @param msgHandles 消息句柄
     * @return
     */
    @DeleteMapping("/message/cancel")
    public CommonResponse<?> cancelMsg(@RequestParam Set<String> msgHandles);



    /************************************************网关服务-start**********************************************************/
    /**
     * 分页获取网关信息
     * @param page 页码
     * @param num 每页数据量
     * @param name 网关名称
     * @return
     */
    @GetMapping("/gateway/north/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetGatewayPageRsp>> getGatewayByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, String name);


    /**
     * 获取网关所有名称
     * @return
     */
    @GetMapping("/gateway/north/name")
    public CommonResponse<List<GetGatewayNameRsp>> getGatewayName(Long gatewayId);


    /**
     * 修改网关信息
     * @param req 修改网关信息请求体
     * @return
     */
    @PutMapping("/gateway/north/update")
    public CommonResponse<?> updateGateway(@RequestBody PutGatewayReq req);
}
