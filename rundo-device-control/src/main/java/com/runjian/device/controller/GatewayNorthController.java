package com.runjian.device.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.north.GatewayNorthService;
import com.runjian.device.vo.request.PutGatewayReq;
import com.runjian.device.vo.response.GetGatewayByIdsRsp;
import com.runjian.device.vo.response.GetGatewayNameRsp;
import com.runjian.device.vo.response.GetGatewayPageRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/2/6 17:24
 */
@RestController
@RequestMapping(("/gateway/north"))
public class GatewayNorthController {

    @Autowired
    private GatewayNorthService gatewayNorthService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 根据网关id获取数据
     * @param gatewayIds
     * @param isIn
     * @return
     */
    @GetMapping("/data/ids")
    public CommonResponse<PageInfo<GetGatewayByIdsRsp>> getGatewayByIds(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                        @RequestParam List<Long> gatewayIds, @RequestParam Boolean isIn, String name){

        return CommonResponse.success(gatewayNorthService.getGatewayByIds(page, num, gatewayIds, isIn, name));
    }

    /**
     * 分页获取网关信息
     * @param page 页码
     * @param num 每页数据量
     * @param name 网关名称
     * @return
     */
    @GetMapping("/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetGatewayPageRsp>> getGatewayByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, String name){
        return CommonResponse.success(gatewayNorthService.getGatewayByPage(page, num, name));
    }

    /**
     * 获取网关所有名称
     * @return
     */
    @GetMapping("/name")
    public CommonResponse<List<GetGatewayNameRsp>> getGatewayName(){
        return CommonResponse.success(gatewayNorthService.getGatewayNameList());
    }

    /**
     * 修改网关信息
     * @param req 修改网关信息请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateGateway(@RequestBody PutGatewayReq req){
        validatorService.validateRequest(req);
        gatewayNorthService.updateGateway(req.getGatewayId(), req.getName());
        return CommonResponse.success();
    }
}
