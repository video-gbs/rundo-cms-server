package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.AuthRbacServerApiFallbackFactory;
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
@FeignClient(value = "auth-rbac",fallbackFactory= AuthRbacServerApiFallbackFactory.class)
public interface AuthRbacServerApi {

    /**
     * 批量添加资源
     * @param req 批量添加资源请求体
     * @return
     */
    @PostMapping("/resource/batch/add")
    CommonResponse<?> batchAddResource(@RequestBody PostBatchResourceReq req);


    /**
     * 批量添加资源
     * @param req 批量添加资源请求体
     * @return
     */
    @PostMapping("/resource/batch/add/kv")
    CommonResponse<?> batchAddResourceKv(@RequestBody PostBatchResourceKvReq req);

    /**
     * 修改资源
     * @param req 修改资源请求体
     * @return
     */
    @PutMapping("/resource/update/kv")
    CommonResponse<?> updateResourceKv(@RequestBody PutResourceReq req);

    /**
     * 删除资源
     * @param resourceId 资源id
     * @return
     */
    @DeleteMapping("/resource/delete")
    @Deprecated
    CommonResponse<?> delete(@RequestParam Long resourceId);

    /**
     * 根据资源信息删除
     * @param resourceKey
     * @param resourceValue
     * @return
     */
    @DeleteMapping("/resource/delete/kv")
    CommonResponse<?> deleteByResourceValue(@RequestParam String resourceKey,@RequestParam String resourceValue);
    /**
     * 分页查询资源
     * @param resourceKey 资源组
     * @param isIncludeResource 是否包含资源数据
     * @return
     */
    @GetMapping("/auth/user/resource")
    CommonResponse<Object> getResourcePage(@RequestParam String resourceKey, @RequestParam Boolean isIncludeResource);

    /**
     * 资源父子级别移动--移动
     * @param req 资源父子移动请求体
     * @return
     */
    @PutMapping("/resource/move/fs")
    @Deprecated
    public CommonResponse<?> fsMove(@RequestBody PutResourceFsMoveReq req);


    /**
     * 资源父子级别移动--移动
     * @param req 资源父子移动请求体
     * @return
     */
    @PutMapping("/resource/move/fs/kv")
    public CommonResponse<?> moveResourceValue(@RequestBody ResourceFsMoveKvReq req);
    /**
     * 部门的兄弟节点移动--排序
     * @param req 部门兄弟节点移动请求体
     * @return
     */
    @PutMapping("/resource/move/bt/kv")
    public CommonResponse<?> btMoveKv(@RequestBody PutResourceBtMoveReq req);

    /**
     * 获取目录下的所有资源数据
     * @param pid 父id
     * @return
     */
    @GetMapping("/auth/user/resource/pid")
    public CommonResponse<List<GetCatalogueResourceRsp>> getCatalogueResourceRsp(@RequestParam Long pid, @RequestParam Boolean isIncludeChild);

}
