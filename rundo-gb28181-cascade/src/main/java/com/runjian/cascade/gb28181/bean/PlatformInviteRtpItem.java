package com.runjian.cascade.gb28181.bean;

import lombok.Data;

/**
 * 级联点播的信息获取
 * @author chenjialing
 */
@Data
public class PlatformInviteRtpItem {



    /**
     * 平台id
     */
    private String platformId;

    /**
     * 本平台的对外交互的ip
     */
    private String platformSdp;

     /**
     * 对应设备id
     */
    private String deviceId;


   /**
     * 通道id
     */
    private String channelId;
    /**
     * 推流ip
     */
    private String superiorIp;

    /**
     * 推流端口
     */
    private int superiorPort;

    /**
     * 推流标识
     */
    private String ssrc;

    /**
     * 推流状态
     * 0 等待设备推流上来
     * 1 等待上级平台回复ack
     * 2 推流中
     */
    private int status = 0;

    /**
     * 自己推流使用的端口
     */
    private int localPort;

    /**
     *  invite 的 callId
     */
    private String callId;

    /**
     *  invite 的 fromTag
     */
    private String fromTag;

    /**
     *  invite 的 toTag
     */
    private String toTag;


    /**
     * 0：udp,1:tcp被动，2：tcp主动
     */
    private int streamProtocal = 0;

    /**
     * 播放类型
     */
    private String sessionName;


    private Long startTime;

    private Long stopTime;



}
