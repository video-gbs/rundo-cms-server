package com.runjian.stream.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.north.GatewayDispatchNorthService;
import com.runjian.stream.vo.request.PostDispatchBindingGatewayReq;
import com.runjian.stream.vo.request.PostGatewayBindingDispatchReq;
import com.runjian.stream.vo.response.GetGatewayByIdsRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/2/9 10:49
 */
@RestController
@RequestMapping("/gateway-dispatch/north")
public class GatewayDispatchNorthController {

    @Autowired
    private GatewayDispatchNorthService gatewayDispatchNorthService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 获取网关绑定的流媒体服务id
     * @param gatewayId 网关id
     * @return
     */
    @GetMapping("/gateway/data")
    public CommonResponse<Long> getDispatchIdByGatewayId(@RequestParam Long gatewayId){
        return CommonResponse.success(gatewayDispatchNorthService.getDispatchIdByGatewayId(gatewayId));
    }

    /**
     * 获取流媒体服务绑定的网关
     * @param page
     * @param num
     * @param dispatchId
     * @param name
     * @return
     */
    @GetMapping("/dispatch/data/in")
    public CommonResponse<PageInfo<GetGatewayByIdsRsp>> getGatewayByDispatchIdIn(@RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "10") int num,
                                                                               @RequestParam Long dispatchId, String name){
        return CommonResponse.success(gatewayDispatchNorthService.getGatewayBindingDispatchId(page, num, dispatchId, name));
    }

    /**
     * 获取流媒体服务未绑定的网关
     * @param page
     * @param num
     * @param dispatchId
     * @param name
     * @return
     */
    @GetMapping("/dispatch/data/not-in")
    public CommonResponse<PageInfo<GetGatewayByIdsRsp>> getGatewayByDispatchIdNotIn(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                               @RequestParam Long dispatchId, String name){
        return CommonResponse.success(gatewayDispatchNorthService.getGatewayNotBindingDispatchId(page, num, dispatchId, name));
    }


    /**
     * 网关绑定调度服务
     * @param req 网关绑定流媒体服务请求体
     * @return
     */
    @PostMapping("/gateway/binding")
    public CommonResponse<?> gatewayBindingDispatch(@RequestBody PostGatewayBindingDispatchReq req){
        validatorService.validateRequest(req);
        gatewayDispatchNorthService.gatewayBindingDispatch(req.getGatewayId(), req.getDispatchId());
        return CommonResponse.success();
    }

    /**
     * 调度服务绑定网关
     * @param req 流媒体服务绑定网关请求体
     * @return
     */
    @PostMapping("/dispatch/binding")
    public CommonResponse<?> dispatchBindingGateway(@RequestBody PostDispatchBindingGatewayReq req){
        validatorService.validateRequest(req);
        gatewayDispatchNorthService.dispatchBindingGateway(req.getDispatchId(), req.getGatewayIds());
        return CommonResponse.success();
    }

    /**
     * 调度服务取消绑定网关
     * @param req 流媒体服务绑定网关请求体
     * @return
     */
    @PostMapping("/dispatch/unbinding")
    public CommonResponse<?> dispatchUnBindingGateway(@RequestBody PostDispatchBindingGatewayReq req){
        validatorService.validateRequest(req);
        gatewayDispatchNorthService.dispatchUnBindingGateway(req.getDispatchId(), req.getGatewayIds());
        return CommonResponse.success();
    }

}
