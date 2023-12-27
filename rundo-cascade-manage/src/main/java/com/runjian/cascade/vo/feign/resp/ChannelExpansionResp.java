package com.runjian.cascade.vo.feign.resp;

import lombok.Data;


/**
 * @author chenjialing
 */
@Data
public class ChannelExpansionResp {
    private Long id;

    private Long deviceExpansionId;

//    //通道名称
    private String channelName;

//    //外观类型
    private Integer ptzType;

//    //状态值
    private Integer onlineState;


//    //ip
    private String ip;

//    //端口
    private Integer port;

//    //通道编码
    private String channelCode;

    //通道编码
    private String gb28181Code;


    //设备厂商
    private String manufacturer;

    //通道类型：0视频，1音频，2告警
    private Integer channelType;


    //朝向
    private String faceLocation;


    //安装地点
    private String installLocation;

    //高度
    private Double height;


    //安防区域id
    private Long videoAreaId;

    //经度
    private String longitude;

    //纬度
    private String latitude;

    //删除标记
    private Integer deleted;








}
