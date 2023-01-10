package com.runjian.device.vo.request;

import com.runjian.common.constant.CommonEnum;
import com.runjian.device.entity.GatewayInfo;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
/**
 * 设备注册请求体
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Data
public class PostGatewaySignInReq {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空")
    @Range(min = 1, message = "非法主键Id")
    private Long id;

    /**
     * 网关唯一序列号 网关ID
     */
    @NotBlank(message = "主键id不能为空")
    @Size(max = 50, message = "非法网关序列号")
    private String serialNum;

    /**
     * 网关名称
     */
    private String name;

    /**
     * 注册类型 1-MQ  2-RETFUL
     */
    @NotNull(message = "注册类型不能为空")
    @Range(min = 1, max = 2, message = "非法注册类型")
    private Integer signType;

    /**
     * 网关类型
     */
    @NotNull(message = "网关类型不能为空")
    @Range(min = 1, max = 2, message = "非法网关类型")
    private Integer gatewayType;

    /**
     * 协议
     */
    @NotBlank(message = "协议不能为空")
    @Size(max = 30, message = "非法协议")
    private String protocol;

    /**
     * ip地址
     */
    @NotBlank(message = "ip地址不能为空")
    @Size(max = 30, message = "非法ip地址")
    private String ip;

    /**
     * 端口
     */
    @NotBlank(message = "端口不能为空")
    @Size(max = 10, message = "非法端口")
    private String port;

    /**
     * 心跳过期时间不能为空
     */
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime outTime;

    /**
     * 转化为gateway对象
     * @return
     */
    public GatewayInfo toGatewayInfo() {
        GatewayInfo gatewayInfo = new GatewayInfo();
        LocalDateTime nowTime = LocalDateTime.now();
        BeanUtils.copyProperties(this, gatewayInfo);
        gatewayInfo.setOnline(CommonEnum.ENABLE.getCode());
        gatewayInfo.setCreateTime(nowTime);
        gatewayInfo.setUpdateTime(nowTime);
        return gatewayInfo;
    }
}
