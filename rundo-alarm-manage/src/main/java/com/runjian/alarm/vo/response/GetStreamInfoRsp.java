package com.runjian.alarm.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/11/14 10:50
 */
@Data
public class GetStreamInfoRsp {


    /**
     * 0：ws-flv,1:http-flv;
     */
    private Integer playProtocolType = 0;

    private String streamId;

    private String httpFlv;

    private String httpsFlv;

    private String wsFlv;

    private String wssFlv;

    private String httpFmp4;

    private String httpsFmp4;

    private String wsFmp4;

    private String wssFmp4;

    private String httpHls;

    private String httpsHls;

    private String wsHls;

    private String wssHls;

    private String httpTs;

    private String httpsTs;

    private String wsTs;

    private String wssTs;

    private String rtmp;

    private String rtmps;

    private String rtsp;

    private String rtsps;

    private String rtc;

    private String rtcs;

    private String mediaServerId;




}
