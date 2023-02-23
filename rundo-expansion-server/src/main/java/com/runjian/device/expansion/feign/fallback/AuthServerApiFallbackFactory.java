package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.feign.AuthServerApi;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.vo.feign.request.DeviceReq;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 调用失败处理工厂 熔断
 *
 * @author huangtongkui
 */
@Slf4j
@Component
public class AuthServerApiFallbackFactory implements FallbackFactory<AuthServerApi> {

    @Override
    public AuthServerApi create(Throwable throwable) {
        return new AuthServerApi() {
            @Override
            public CommonResponse<List<VideoAreaResp>> getVideoAraeList(Long areaId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"权限服务","feign--编码器获取安防通道信息列表失败",areaId, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }

            @Override
            public CommonResponse<VideoAreaResp> getVideoAraeInfo(Long areaId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"权限服务","feign--编码器获取安防通道信息失败",areaId, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }
        };
    }
}
