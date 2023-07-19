package com.runjian.device.expansion.controller.edgeServer;

import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.feign.StreamManageApi;
import com.runjian.device.expansion.vo.feign.request.PostDispatchBindingGatewayReq;
import com.runjian.device.expansion.vo.feign.request.PostGatewayBindingDispatchReq;
import com.runjian.device.expansion.vo.feign.request.PutDispatchExtraDataReq;
import com.runjian.device.expansion.vo.feign.request.PutGatewayReq;
import com.runjian.device.expansion.vo.feign.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenjialing
 */
@Api(tags = "服务管理")
@Slf4j
@RestController
@RequestMapping("/expansion")
public class EdgeServerController {

    @Autowired
    DeviceControlApi deviceControlApi;

    @Autowired
    StreamManageApi streamManageApi;

    /**
     * 分页获取网关信息
     * @param page 页码
     * @param num 每页数据量
     * @param name 网关名称
     * @return
     */
    @ApiOperation("分页获取网关信息")
    @GetMapping("/gateway/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<Object> getGatewayByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, String name){
        return deviceControlApi.getGatewayByPage(page, num, name);

    }

    /**
     * 获取网关所有名称
     * @return
     */
    @ApiOperation("获取网关所有名称")
    @GetMapping("/gateway/name")
    public CommonResponse<List<GetGatewayNameRsp>> getGatewayName(@RequestParam(required = false) Long gatewayId){
        return deviceControlApi.getGatewayName(gatewayId);
    }


    /**
     * 修改网关信息
     * @param req 修改网关信息请求体
     * @return
     */
    @ApiOperation("修改网关信息")
    @PutMapping("/gateway/update")
    public CommonResponse<?> updateGateway(@RequestBody PutGatewayReq req){
        return deviceControlApi.updateGateway(req);
    }


    /**
     * 获取所有调度服务的名称
     * @return
     */
    @ApiOperation("获取所有调度服务的名称")
    @GetMapping("/dispatch/name")
    public CommonResponse<List<GetDispatchNameRsp>> getDispatchName(){
        return streamManageApi.getDispatchName();
    }

    /**
     * 获取调度服务信息
     * @param page 页码
     * @param num 每页数量
     * @param name 名称
     * @return 分页数据
     */
    @ApiOperation("获取调度服务信息")
    @GetMapping("/dispatch/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<Object> getDispatchByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer num, String name){

        return streamManageApi.getDispatchByPage(page, num, name);
    }

    /**
     * 修改额外的名字与可访问URL
     * @param req 请求体
     */
    @ApiOperation("修改调度服务额外的名字与可访问URL")
    @PutMapping("/dispatch/update")
    public CommonResponse<?> updateExtraData(@RequestBody PutDispatchExtraDataReq req){
        return streamManageApi.updateExtraData(req);
    }

    /*****************************服务的绑定***************************************************/

    /**
     * 获取网关绑定的流媒体服务id
     * @param gatewayId 网关id
     * @return
     */
    @ApiOperation("获取网关绑定的流媒体服务id")
    @GetMapping("/gateway-dispatch/gateway/data")
    public CommonResponse<Long> getDispatchIdByGatewayId(@RequestParam Long gatewayId){
        return streamManageApi.getDispatchIdByGatewayId(gatewayId);
    }

    /**
     * 获取流媒体服务绑定的网关
     * @param page
     * @param num
     * @param dispatchId
     * @param name
     * @return
     */
    @ApiOperation("获取流媒体服务绑定的网关")
    @GetMapping("/gateway-dispatch/dispatch/data/in")
    public CommonResponse<Object> getGatewayByDispatchIdIn(@RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "10") int num,
                                                                                 @RequestParam Long dispatchId, String name){
        return streamManageApi.getGatewayByDispatchIdIn(page, num, dispatchId, name);
    }

    /**
     * 获取流媒体服务未绑定的网关
     * @param page
     * @param num
     * @param dispatchId
     * @param name
     * @return
     */
    @ApiOperation("获取流媒体服务未绑定的网关")
    @GetMapping("/gateway-dispatch/dispatch/data/not-in")
    public CommonResponse<Object> getGatewayByDispatchIdNotIn(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                                    @RequestParam Long dispatchId, String name){
        return streamManageApi.getGatewayByDispatchIdNotIn(page, num, dispatchId, name);
    }


    /**
     * 网关绑定调度服务
     * @param req 网关绑定流媒体服务请求体
     * @return
     */
    @ApiOperation("网关绑定调度服务")
    @PostMapping("/gateway-dispatch/gateway/binding")
    public CommonResponse<?> gatewayBindingDispatch(@RequestBody PostGatewayBindingDispatchReq req){
        return streamManageApi.gatewayBindingDispatch(req);
    }

    /**
     * 调度服务绑定网关
     * @param req 流媒体服务绑定网关请求体
     * @return
     */
    @ApiOperation("调度服务绑定网关")
    @PostMapping("/gateway-dispatch/dispatch/binding")
    public CommonResponse<?> dispatchBindingGateway(@RequestBody PostDispatchBindingGatewayReq req){
        return streamManageApi.dispatchBindingGateway(req);
    }

    /**
     * 调度服务取消绑定网关
     * @param req 流媒体服务绑定网关请求体
     * @return
     */
    @ApiOperation("调度服务取消绑定网关")
    @PostMapping("/gateway-dispatch/dispatch/unbinding")
    public CommonResponse<?> dispatchUnBindingGateway(@RequestBody PostDispatchBindingGatewayReq req){
        return streamManageApi.dispatchUnBindingGateway(req);
    }

}
