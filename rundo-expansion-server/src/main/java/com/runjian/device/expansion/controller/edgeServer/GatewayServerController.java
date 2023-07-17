package com.runjian.device.expansion.controller.edgeServer;

import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.feign.request.PutGatewayReq;
import com.runjian.device.expansion.vo.feign.response.GetGatewayNameRsp;
import com.runjian.device.expansion.vo.feign.response.GetGatewayPageRsp;
import com.runjian.device.expansion.vo.response.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenjialing
 */
@Api(tags = "服务管理")
@Slf4j
@RestController
@RequestMapping("/expansion/gateway")
public class GatewayServerController {

    @Autowired
    DeviceControlApi deviceControlApi;

    /**
     * 分页获取网关信息
     * @param page 页码
     * @param num 每页数据量
     * @param name 网关名称
     * @return
     */
    @ApiOperation("分页获取网关信息")
    @GetMapping("/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetGatewayPageRsp>> getGatewayByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, String name){
        return deviceControlApi.getGatewayByPage(page, num, name);

    }

    /**
     * 获取网关所有名称
     * @return
     */
    @ApiOperation("获取网关所有名称")
    @GetMapping("/name")
    public CommonResponse<List<GetGatewayNameRsp>> getGatewayName(Long gatewayId){
        return deviceControlApi.getGatewayName(gatewayId);
    }


    /**
     * 修改网关信息
     * @param req 修改网关信息请求体
     * @return
     */
    @ApiOperation("修改网关信息")
    @PutMapping("/update")
    public CommonResponse<?> updateGateway(@RequestBody PutGatewayReq req){
        return deviceControlApi.updateGateway(req);
    }

}
