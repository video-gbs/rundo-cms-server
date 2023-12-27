package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 通道详情
 * @author Miracle
 * @date 2023/1/9 10:16
 */
@Data
public class PostChannelDetailReq {

    /**
     * 通道id
     */
    @NotNull(message = "通道id不能为空")
    private Long channelId;

    /**
     * 订阅类型
     */
    @NotNull(message = "订阅类型不能为空")
    @Range(min = 0, max = 5, message = "订阅类型不正确")
    private Integer subscribeType;

    /**
     * 原始id
     */
    @NotBlank(message = "通道原始id不能为空")
    private String originId;

    /**
     * 节点id
     */
    private String nodeOriginId;

    /**
     * 通道类型
     */
    private Integer channelType;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 在线状态
     */
    private Integer onlineState;

    /**
     * 名称
     */
    private String name;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 固件版本
     */
    private String firmware;

    /**
     * 云台类型
     */
    private Integer ptzType;
}
