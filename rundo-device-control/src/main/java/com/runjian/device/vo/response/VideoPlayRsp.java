package com.runjian.device.vo.response;

import lombok.Data;


/**
 * 视频播放返回体
 * @author Miracle
 * @date 2023/1/9 9:55
 */
@Data
public class VideoPlayRsp {

    /**
     * 流id
     */
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
}

