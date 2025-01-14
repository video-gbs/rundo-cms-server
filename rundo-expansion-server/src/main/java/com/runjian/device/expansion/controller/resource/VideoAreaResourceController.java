package com.runjian.device.expansion.controller.resource;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.feign.AuthRbacServerApi;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import com.runjian.device.expansion.vo.request.PostVideoAreaReq;
import com.runjian.device.expansion.vo.request.PutVideoAreaReq;
import com.runjian.device.expansion.vo.request.VideoResourceFsMoveKvReq;
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
@Api(tags = "安防通道操作")
@Slf4j
@RestController
@RequestMapping("/expansion/videoArea")
public class VideoAreaResourceController {
    @Autowired
    AuthRbacServerApi authRbacServerApi;

    @Autowired
    private ValidatorService validatorService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防通道添加")
    public CommonResponse<?> add(@RequestBody PostVideoAreaReq req) {
        validatorService.validateRequest(req);

        PostBatchResourceKvReq postBatchResourceKvReq = new PostBatchResourceKvReq();
        postBatchResourceKvReq.setResourceType(1);
        postBatchResourceKvReq.setResourceKey(req.getResourceKey());
        postBatchResourceKvReq.setParentResourceValue(req.getPResourceValue());

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        String s = UuidUtils.generateUuid();
        stringStringHashMap.put(s, req.getName());
        postBatchResourceKvReq.setResourceMap(stringStringHashMap);
        CommonResponse<?> commonResponse = authRbacServerApi.batchAddResourceKv(postBatchResourceKvReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;


    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防通道编辑")
    public CommonResponse<?> edit(@RequestBody PutResourceReq req) {
        validatorService.validateRequest(req);
        CommonResponse<?> commonResponse = authRbacServerApi.updateResourceKv(req);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防通道删除")
    public CommonResponse<?> delete(@RequestParam String resourceKey,@RequestParam String resourceValue) {
        CommonResponse<?> commonResponse = authRbacServerApi.deleteByResourceValue(resourceKey,resourceValue);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }

    @PutMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防通道移动")
    public CommonResponse<?> move(@RequestBody VideoResourceFsMoveKvReq req) {
        validatorService.validateRequest(req);
        ResourceFsMoveKvReq resourceFsMoveKvReq = new ResourceFsMoveKvReq();
        BeanUtil.copyProperties(req,resourceFsMoveKvReq);
        resourceFsMoveKvReq.setParentResourceValue(req.getPResourceValue());
        CommonResponse<?> commonResponse = authRbacServerApi.moveResourceValue(resourceFsMoveKvReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }

    @PutMapping(value = "/sort", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("安防通道排序")
    public CommonResponse<?> sort(@RequestBody PutResourceBtMoveReq req) {
        validatorService.validateRequest(req);
        CommonResponse<?> commonResponse = authRbacServerApi.btMoveKv(req);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }


}
