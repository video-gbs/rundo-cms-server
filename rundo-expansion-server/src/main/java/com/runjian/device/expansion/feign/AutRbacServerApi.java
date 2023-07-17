package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.AuthServerApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.PostBatchResourceReq;
import com.runjian.device.expansion.vo.feign.request.PutResourceReq;
import com.runjian.device.expansion.vo.feign.response.GetResourceTreeRsp;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "auth-rbac",fallbackFactory= AuthServerApiFallbackFactory.class)
public interface AutRbacServerApi {

    /**
     * 批量添加资源
     * @param req 批量添加资源请求体
     * @return
     */
    @PostMapping("/resource/batch/add")
    CommonResponse<?> batchAddResource(@RequestBody PostBatchResourceReq req);



    /**
     * 修改资源
     * @param req 修改资源请求体
     * @return
     */
    @PutMapping("/resource/update")
    CommonResponse<?> updateResource(@RequestBody PutResourceReq req);

    /**
     * 删除资源
     * @param resourceId 资源id
     * @return
     */
    @DeleteMapping("/resource/delete")
    CommonResponse<?> delete(@RequestParam Long resourceId);

    /**
     * 分页查询资源
     * @param resourceKey 资源组
     * @param isIncludeResource 是否包含资源数据
     * @return
     */
    @GetMapping("/resource/tree")
    CommonResponse<GetResourceTreeRsp> getResourcePage(@RequestParam String resourceKey, @RequestParam Boolean isIncludeResource);

}
