package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.AuthServerApiFallbackFactory;
import com.runjian.device.expansion.feign.fallback.DeviceControlApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "rundo-auth-server",fallbackFactory= AuthServerApiFallbackFactory.class)
public interface AuthServerApi {


    /**
     * 权限服务 安防区域全部子集信息
     * @param areaId
     * @return
     */
    @PostMapping(value = "/rundoAuthServer/videoArae/getList")
    CommonResponse<List<VideoAreaResp>> getVideoAraeList(@RequestParam Integer areaId);

    /**
     * 控制服务 设备添加
     * @param areaId
     * @return
     */
    @PostMapping(value = "/rundoAuthServer/videoArae/getById")
    CommonResponse<VideoAreaResp> getVideoAraeInfo(@RequestParam Integer areaId);
}
