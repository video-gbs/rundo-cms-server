package com.runjian.device.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.vo.feign.StreamPlayReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "stream-manage")
public interface StreamManageApi {


    /**
     * 申请播放
     * @param req 流播放申请请求体
     * @return
     */
    @PostMapping("/stream/north/play/apply")
    CommonResponse<Map<String, Object>> applyPlay(@RequestBody StreamPlayReq req);

}
