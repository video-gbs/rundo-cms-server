package com.runjian.device.expansion.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.DeviceControlApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.DeviceReq;
import com.runjian.device.expansion.vo.feign.response.GetChannelByPageRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "device-control",fallbackFactory= DeviceControlApiFallbackFactory.class)
public interface DeviceControlApi {


    /**
     * 控制服务 设备添加
     * @param deviceReq
     * @return
     */
    @PostMapping(value = "/device/north/add",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<Long> deviceAdd(@RequestBody DeviceReq deviceReq);

    /**
     * 控制服务 设备删除
     * @param deviceId
     * @return
     */
    @DeleteMapping("/device/north/delete")
    CommonResponse deleteDevice(@RequestParam Long deviceId);

    /**
     * 控制服务 通道添加状态修改
     * @param channelIdList
     * @return
     */
    @PostMapping(value = "/channel/north/sign/success",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<Boolean> channelSignSuccess(@RequestBody List<Long> channelIdList);


    /**
     * 控制服务 通道待添加列表
     * @param page
     * @param num
     * @param nameOrOriginId
     * @return
     */
    @GetMapping(value = "/channel/north/page",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<IPage<GetChannelByPageRsp>> getChannelByPage(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10") int num, String nameOrOriginId);


}
