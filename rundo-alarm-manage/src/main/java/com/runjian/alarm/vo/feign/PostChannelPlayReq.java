package com.runjian.alarm.vo.feign;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/11/14 11:14
 */
@Data
public class PostChannelPlayReq {

    public PostChannelPlayReq(Long channelId){
        this.channelId = channelId;
    }

    private Long channelId;

    private Integer enableAudio = 0;

    private Boolean ssrcCheck = true;

    private Integer streamType = 2;

    private Integer playType = 2;

    private Integer recordState = 0;

    private Integer autoCloseState = 1;

    /**
     * 码流id(主子码流)
     */
    @NotNull(message = "主子码流选择不能为空")
    @Range(min = 0, message = "非法主子码流")
    private Integer bitStreamId = 0;








}
