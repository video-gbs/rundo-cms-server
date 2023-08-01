package com.runjian.device.expansion.controller.resource;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.AuthRbacServerApi;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import com.runjian.device.expansion.vo.request.PostVideoAreaReq;
import com.runjian.device.expansion.vo.request.PutVideoAreaReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author chenjialing
 */
@Api(tags = "安防区域操作")
@Slf4j
@RestController
@RequestMapping("/expansion/videoArea")
public class VideoAreaResourceController {
    @Autowired
    AuthRbacServerApi authRbacServerApi;


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防区域添加")
    public CommonResponse<?> add(@RequestBody PostVideoAreaReq req) {
        PostBatchResourceReq postBatchResourceReq = new PostBatchResourceReq();
        postBatchResourceReq.setResourceType(1);
        postBatchResourceReq.setResourcePid(req.getResourcePid());
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        String s = UuidUtils.generateUuid();
        stringStringHashMap.put(s, req.getName());
        postBatchResourceReq.setResourceMap(stringStringHashMap);
        authRbacServerApi.batchAddResource(postBatchResourceReq);
        PutRefreshUserResourceReq putRefreshUserResourceReq = new PutRefreshUserResourceReq();
        putRefreshUserResourceReq.setResourceKey(req.getResourceKey());
        return authRbacServerApi.refreshUserResource(putRefreshUserResourceReq);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防区域编辑")
    public CommonResponse<?> add(@RequestBody PutResourceReq req) {
        authRbacServerApi.updateResourceKv(req);
        PutRefreshUserResourceReq putRefreshUserResourceReq = new PutRefreshUserResourceReq();
        putRefreshUserResourceReq.setResourceKey(req.getResourceKey());
        return authRbacServerApi.refreshUserResource(putRefreshUserResourceReq);
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防区域删除")
    public CommonResponse<?> delete(@RequestParam String resourceKey,@RequestParam String resourceValue) {
        authRbacServerApi.deleteByResourceValue(resourceKey,resourceValue);
        PutRefreshUserResourceReq putRefreshUserResourceReq = new PutRefreshUserResourceReq();
        putRefreshUserResourceReq.setResourceKey(resourceKey);
        return authRbacServerApi.refreshUserResource(putRefreshUserResourceReq);

    }

    @PutMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防区域移动")
    public CommonResponse<?> move(@RequestBody ResourceFsMoveKvReq req) {

        authRbacServerApi.moveResourceValue(req);
        PutRefreshUserResourceReq putRefreshUserResourceReq = new PutRefreshUserResourceReq();
        putRefreshUserResourceReq.setResourceKey(req.getResourceKey());
        return authRbacServerApi.refreshUserResource(putRefreshUserResourceReq);
    }

    @PutMapping(value = "/sort", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防区域排序")
    public CommonResponse<?> sort(@RequestBody PutResourceBtMoveReq req) {
        return authRbacServerApi.btMoveKv(req);
    }


}
