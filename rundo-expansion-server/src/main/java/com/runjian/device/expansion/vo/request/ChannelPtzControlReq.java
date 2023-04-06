package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * ptz/设备控制操作请求指令
 * @author chenjialing
 */
@Data
@ApiModel(value = "ptz操作指令")
public class ChannelPtzControlReq {


    @ApiModelProperty("主键ID")
    @NotNull(message = "通道id不能为空")
    private Long channelExpansionId;

    @ApiModelProperty("指令操作值;0-255")
    @Range(min = 0,max = 255,message = "指令操作值需要为;0-255")
    private Integer operationValue;

    @ApiModelProperty("指令操作类型--" +
            "    PTZ_LEFT(2,\"PTZ_LEFT\"),\n" +
            "    PTZ_RIGHT(1,\"PTZ_RIGHT\"),\n" +
            "    PTZ_UP(8,\"PTZ_UP\"),\n" +
            "    PTZ_DOWN(4,\"PTZ_DOWN\"),\n" +
            "    PTZ_UPLEFT(10,\"PTZ_UPLEFT\"),\n" +
            "    PTZ_UPRIGHT(9,\"PTZ_UPRIGHT\"),\n" +
            "    PTZ_DOWNLEFT(6,\"PTZ_DOWNLEFT\"),\n" +
            "    PTZ_DOWNRIGHT(5,\"PTZ_DOWNRIGHT\"),\n" +
            "    //倍率缩小\n" +
            "    ZOOM_IN(20,\"ZOOM_IN\"),\n" +
            "    //倍率放大\n" +
            "    ZOOM_OUT(10,\"ZOOM_OUT\"),\n" +
            "\n" +
            "    PTZ_STOP(0,\"PTZ_STOP\"),\n" +
            "    //预置位\n" +
            "    PRESET_SET(129,\"PRESET_SET\"),\n" +
            "    PRESET_INVOKE(130,\"PRESET_INVOKE\"),\n" +
            "    PRESET_DEL(131,\"PRESET_DEL\"),\n" +
            "\n" +
            "    //F1 指令\n" +
            "    //光圈缩小放大\n" +
            "    IRIS_REDUCE(48,\"IRIS_REDUCE\"),\n" +
            "    IRIS_GROW(44,\"IRIS_GROW\"),\n" +
            "    //聚焦近远\n" +
            "    FOCUS_FAR(41,\"FOCUS_FAR\"),\n" +
            "    FOCUS_NEAR(42,\"FOCUS_NEAR\"),\n" +
            "    //F1停止stop\n" +
            "    IRISE_AND_FOCUS_STOP(40,\"IRISE_AND_FOCUS_STOP\")," +
            ";0-255")
    @Range(min = 0,max = 255,message = "指令操作类型需要为;0-255")
    private Integer ptzOperationType;


}
