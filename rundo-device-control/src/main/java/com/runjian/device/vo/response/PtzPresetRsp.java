package com.runjian.device.vo.response;

import lombok.Data;

/**
 * 预置位返回
 * @author Miracle
 * @date 2023/4/6 16:05
 */
@Data
public class PtzPresetRsp {

    /**
     * 预置位id
     */
    private Integer presetId;

    /**
     * 预置位名称
     */
    private String presetName;
}
