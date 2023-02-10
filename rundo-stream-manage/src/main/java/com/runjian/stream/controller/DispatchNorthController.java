package com.runjian.stream.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.north.DispatchNorthService;
import com.runjian.stream.vo.response.GetDispatchNameRsp;
import com.runjian.stream.vo.response.GetDispatchRsp;
import com.runjian.stream.vo.response.PutDispatchExtraDataReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/7 20:01
 */
@RestController
@RequestMapping("/dispatch/north")
public class DispatchNorthController {

    @Autowired
    private DispatchNorthService dispatchNorthService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 获取所有调度服务的名称
     * @return
     */
    @GetMapping("/name")
    public CommonResponse<List<GetDispatchNameRsp>> getDispatchName(){
        return CommonResponse.success(dispatchNorthService.getDispatchName());
    }

    /**
     * 获取调度服务信息
     * @param page 页码
     * @param num 每页数量
     * @param name 名称
     * @return 分页数据
     */
    @GetMapping("/page")
    public CommonResponse<PageInfo<GetDispatchRsp>> getDispatchByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer num, String name){
        return CommonResponse.success(dispatchNorthService.getDispatchByPage(page, num, name));
    }

    /**
     * 修改额外的名字与可访问URL
     * @param req 请求体
     */
    @PutMapping("/update")
    public CommonResponse<?> updateExtraData(@RequestBody PutDispatchExtraDataReq req){
        validatorService.validateRequest(req);
        dispatchNorthService.updateExtraData(req.getDispatchId(), req.getName(), req.getUrl());
        return CommonResponse.success();
    }

}
