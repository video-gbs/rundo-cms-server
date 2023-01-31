package com.runjian.parsing.vo.request;


import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import java.util.Map;
import java.util.Objects;

/**
 * 通用北向消息传输体
 * @author Miracle
 * @date 2023/1/9 15:11
 */
@Data
public class DeviceControlReq implements ValidatorFunction {

    /**
     * 设备ID
     */
    @Range(min = 1, message = "设备ID不能为空")
    private Long deviceId;

    /**
     * 网关ID
     */
    @Range(min = 1, message = "非法网关Id")
    private Long gatewayId;

    /**
     * 通道id
     */
    @Range(min = 1, message = "非法通道id")
    private Long channelId;

    /**
     * 数据集合
     */
    private Map<String, Object> dataMap;

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (Objects.isNull(deviceId) & Objects.isNull(channelId) & Objects.isNull(gatewayId)){
            result.setHasErrors(true);
            result.getErrorMsgMap().put("请求参数错误", "主键id缺失");
        }
    }

}
