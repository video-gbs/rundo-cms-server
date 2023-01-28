package com.runjian.parsing.vo.response;

import lombok.Data;

import java.util.List;

/**
 * 用户主动同步返回值
 * @author Miracle
 * @date 2023/1/9 9:55
 */
@Data
public class ChannelSyncRsp {

    /**
     * 设备通道总数
     */
    private Integer total;

    /**
     * 同步成功的通道数
     */
    private Integer num;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 同步的数据
     */
    private List<ChannelDetail> channelDetailList;


    /**
     * 通道详情
     */
    @Data
    public static class ChannelDetail {

        /**
         * 通道id
         */
        private String channelId;

        /**
         * ip地址
         */
        private String ip;

        /**
         * 端口
         */
        private String port;

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


}
