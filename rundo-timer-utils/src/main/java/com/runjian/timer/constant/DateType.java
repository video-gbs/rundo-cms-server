package com.runjian.timer.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/10/17 9:23
 */
@Getter
@AllArgsConstructor
public enum DateType {

    MONDAY(1, "星期一"),
    TUESDAY(2, "星期二"),
    WEDNESDAY(3, "星期三"),
    THURSDAY(4, "星期四"),
    FRIDAY(5, "星期五"),
    SATURDAY(6, "星期六"),
    SUNDAY(7, "星期日");

    private final Integer code;

    private final String msg;

    public static String getMsgByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DateType dateType : DateType.values()) {
            if (code.equals(dateType.getCode())){
                return dateType.msg;
            }
        }
        return null;
    }
}
