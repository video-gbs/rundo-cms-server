package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Miracle
 * @date 2023/3/28 14:38
 */
@Slf4j
@RestController
@RequestMapping("/common/south")
public class CommonSouthController {

    @PostMapping("/error")
    public CommonResponse<?> errorEvent(@RequestBody String data){
        log.info(LogTemplate.ERROR_LOG_TEMPLATE, "边端错误信息收集接口", "接收到异常", data);
        return CommonResponse.success();
    }
}
