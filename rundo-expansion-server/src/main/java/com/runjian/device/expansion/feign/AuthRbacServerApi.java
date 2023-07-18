package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.AuthServerApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.*;
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
public interface AuthRbacServerApi {

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

    /**
     * 资源父子级别移动--移动
     * @param req 资源父子移动请求体
     * @return
     */
    @PutMapping("/move/fs")
    public CommonResponse<?> fsMove(@RequestBody PutResourceFsMoveReq req);

    /**
     * 部门的兄弟节点移动--排序
     * @param req 部门兄弟节点移动请求体
     * @return
     */
    @PutMapping("/move/bt")
    public CommonResponse<?> btMove(@RequestBody PutResourceBtMoveReq req);

    /**
     * 获取目录下的所有资源数据
     * @param pid 父id
     * @param isIncludeChild 是否包含子目录数据
     * @return
     */
    @GetMapping("/pid")
    public CommonResponse<List<GetCatalogueResourceRsp>> getCatalogueResourceRsp(@RequestParam Long pid, @RequestParam Boolean isIncludeChild);

}
