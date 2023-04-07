package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author Miracle
 * @date 2023/4/6 15:35
 */
@Data
public class Ptz3dReq {

    /**
     * 通道id
     */
    @ApiModelProperty("通道id")
    @Range(min = 1, message = "非法通道id")
    private Long channelExpansionId;

    /**
     * 放大-1 缩小-2
     */
    @ApiModelProperty("放大-1 缩小-2")
    @Range(min = 1, message = "非法操作类型")
    private Integer dragType;

    /**
     * 拉宽长度
     */
    @ApiModelProperty("拉宽长度")
    @Range(min = 1, message = "非法拉宽长度")
    private Integer length;

    /**
     * 拉宽宽度
     */
    @ApiModelProperty("拉宽宽度")
    @Range(min = 1, message = "非法拉宽宽度")
    private Integer width;

    /**
     * 拉框中心的横轴坐标像素值
     */
    @ApiModelProperty("拉框中心的横轴坐标像素值")
    @Range(min = 1, message = "非法拉框中心的横轴坐标像素值")
    private Integer midPointX;

    /**
     * 拉框中心的纵轴坐标像素值
     */
    @ApiModelProperty("拉框中心的纵轴坐标像素值")
    @Range(min = 1, message = "非法拉框中心的纵轴坐标像素值")
    private Integer midPointY;

    /**
     * 拉框长度像素值
     */
    @ApiModelProperty("拉框长度像素值")
    @Range(min = 1, message = "非法拉框长度像素值")
    private Integer lengthX;

    /**
     * 拉框宽度像素值
     */
    @ApiModelProperty("拉框宽度像素值")
    @Range(min = 1, message = "非法拉框宽度像素值")
    private Integer lengthY;
}
