package com.runjian.auth.server.controller.system;

import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName IndexController
 * @Description 门户首页
 * @date 2023-02-07 周二 16:27
 */
@Api(tags = "接口管理")
@Slf4j
@RestController
@RequestMapping("/home")
public class IndexController {

    public CommonResponse<?> index(){

        return CommonResponse.success(null);
    }
}
