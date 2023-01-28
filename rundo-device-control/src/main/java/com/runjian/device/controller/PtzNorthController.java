package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.north.PtzNorthService;
import com.runjian.device.vo.feign.PutPtzControlReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 云台控制北向接口
 * @author Miracle
 * @date 2023/1/9 14:57
 */
@RestController
@RequestMapping("/ptz/north")
public class PtzNorthController {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private PtzNorthService ptzNorthService;

    /**
     * 云台控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/control")
    public CommonResponse ptzControl(@RequestBody PutPtzControlReq req){
        validatorService.validateRequest(req);
        ptzNorthService.ptzControl(req.getChannelId(),req.getCommandCode(),req.getHorizonSpeed(),req.getVerticalSpeed(),req.getZoomSpeed(),req.getTotalSpeed());
        return CommonResponse.success();
    }


}
