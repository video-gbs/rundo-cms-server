package com.runjian.parsing.constant;

import com.runjian.parsing.entity.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态
 * @see TaskInfo#state
 * @author Miracle
 * @date 2023/1/13 11:44
 */

@Getter
@AllArgsConstructor
public enum TaskState {


    RUNNING(0, "执行中"),
    SUCCESS(1, "已完成"),
    ERROR(-1, "异常")

    ;
    private final Integer code;
    private final String msg;

    public static String getMsg(int code){
        if (code == 0){
            return RUNNING.getMsg();
        } else if (code == 1) {
            return SUCCESS.getMsg();
        } else if (code == -1){
            return ERROR.getMsg();
        }
        return null;
    }


}
