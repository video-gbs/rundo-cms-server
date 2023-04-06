package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/4/6 15:35
 */
@Data
public class FeignPtz3dReq {

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 放大-1 缩小-2
     */
    private Integer dragType;

    /**
     * 拉宽长度
     */
    private Integer length;

    /**
     * 拉宽宽度
     */
    private Integer width;

    /**
     * 拉框中心的横轴坐标像素值
     */
    private Integer midPointX;

    /**
     * 拉框中心的纵轴坐标像素值
     */
    private Integer midPointY;

    /**
     * 拉框长度像素值
     */
    private Integer lengthX;

    /**
     * 拉框宽度像素值
     */
    private Integer lengthY;
}
