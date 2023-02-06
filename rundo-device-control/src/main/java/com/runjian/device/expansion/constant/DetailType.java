package com.runjian.device.expansion.constant;

import com.runjian.device.expansion.entity.DetailInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 详细数据类型
 * @see  DetailInfo#type
 * @author Miracle
 * @date 2023/1/9 18:04
 */
@Getter
@AllArgsConstructor
public enum DetailType {
    DEVICE(1, "DEVICE"),
    CHANNEL(2, "CHANNEL");

    private final Integer code;

    private final String msg;


}
