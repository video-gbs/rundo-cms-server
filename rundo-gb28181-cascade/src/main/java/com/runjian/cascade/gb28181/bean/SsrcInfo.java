package com.runjian.cascade.gb28181.bean;

import lombok.Data;

/**
 * @author chenjialing
 */
@Data
public class SsrcInfo {

    private int port;
    private String ssrc;
    private String sdpIp;
    private String streamId;
    private String mediaServerId;
    private String ip;

    public SsrcInfo(){}
    public SsrcInfo(int port, String ssrc, String streamId, String mediaServerId) {
        this.port = port;
        this.ssrc = ssrc;
        this.streamId = streamId;
        this.mediaServerId = mediaServerId;
    }

}
