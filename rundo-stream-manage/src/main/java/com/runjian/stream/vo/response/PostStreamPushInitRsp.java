package com.runjian.stream.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miracle
 * @date 2023/12/7 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStreamPushInitRsp {

    /**
     * 流推ID
     */
    private Long streamPushId;

    /**
     * 本地端口
     */
    private Integer srcPort;
}
