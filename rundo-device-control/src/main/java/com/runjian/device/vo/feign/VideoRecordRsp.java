package com.runjian.device.vo.feign;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/1/29 20:07
 */
@Data
public class VideoRecordRsp {

    /**
     * 数据总数
     */
    private Integer sumNum;

    /**
     * 录像数据数组
     */
    private List<RecordFile> channelRecordList;

    /**
     * 录像文件
     */
    @Data
    public static class RecordFile{

        /**
         * 文件名称
         */
        private String name;

        /**
         * 文件大小
         */
        private String fileSize;

        /**
         * 开始时间
         */
        private LocalDateTime startTime;

        /**
         * 结束时间
         */
        private LocalDateTime endTime;

    }
}
