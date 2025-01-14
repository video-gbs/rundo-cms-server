package com.runjian.device.expansion.controller.gis;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.entity.GisConfig;
import com.runjian.device.expansion.entity.GisVideoAreaConfig;
import com.runjian.device.expansion.service.IGisService;
import com.runjian.device.expansion.vo.request.GisConfigReq;
import com.runjian.device.expansion.vo.request.GisConfigStatusReq;
import com.runjian.device.expansion.vo.request.GisVideoAreaConfigReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenjialing
 */
@Api(tags = "gis管理")
@Slf4j
@RestController
//@CrossOrigin
@RequestMapping("/expansion/gis")
public class GisController {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private IGisService gisService;

    @PostMapping(value = "/save",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("gis配置--编辑/添加接口")
    public CommonResponse<Boolean> save(@RequestBody GisConfigReq request) {

        validatorService.validateRequest(request);
        gisService.save(request);
        return CommonResponse.success();
    }

    @GetMapping(value = "/configList",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("gis配置--列表")
    public CommonResponse<List<GisConfig>> configList() {

        return CommonResponse.success(gisService.list());
    }


    @PutMapping(value = "/statusChange",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("gis配置--状态修改")
    public CommonResponse<Boolean> statusChange(@RequestBody GisConfigStatusReq request) {

        validatorService.validateRequest(request);
        gisService.statusChange(request);
        return CommonResponse.success();
    }

    @GetMapping(value = "/findOneStatusOn",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("gis配置--获取开启得gis地图")
    public CommonResponse<GisConfig> findOneStatusOn() {

        return CommonResponse.success(gisService.findOneStatusOn());
    }

    @PostMapping(value = "/gisVideoAreaSave",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("gis与节点得配置--保存节点与配置的信息")
    public CommonResponse<Long> gisVideoAreaSave(@RequestBody GisVideoAreaConfigReq request) {
        validatorService.validateRequest(request);
        return CommonResponse.success(gisService.gisConfigVideoAreaSave(request));
    }


    @GetMapping(value = "/findVideoAreaOne",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("gis与节点得配置--查询节点得地图信息")
    public CommonResponse<GisVideoAreaConfig> findVideoAreaOne(@RequestParam Long videoAreaId) {

        return CommonResponse.success( gisService.findVideoAreaOne(videoAreaId));
    }

}
