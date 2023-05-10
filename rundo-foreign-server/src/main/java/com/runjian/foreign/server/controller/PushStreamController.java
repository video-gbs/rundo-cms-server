package com.runjian.foreign.server.controller;

import cn.hutool.core.bean.BeanUtil;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.foreign.server.feign.StreamManageApi;
import com.runjian.foreign.server.vo.feign.request.PushStreamCustomLiveFeignReq;
import com.runjian.foreign.server.vo.feign.response.StreamInfo;
import com.runjian.foreign.server.vo.request.PushStreamCustomLiveReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "推流管理")
@Slf4j
@RestController
@RequestMapping("/push/stream")
public class PushStreamController {
    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private StreamManageApi streamManageApi;
    @PostMapping(value = "/live",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("推流点播")
    public CommonResponse<StreamInfo> customLive(@RequestBody PushStreamCustomLiveReq req) {

        validatorService.validateRequest(req);
        PushStreamCustomLiveFeignReq pushStreamCustomLiveFeignReq = new PushStreamCustomLiveFeignReq();
        BeanUtil.copyProperties(req,pushStreamCustomLiveFeignReq);

        return streamManageApi.customLive(pushStreamCustomLiveFeignReq);

    }

}
