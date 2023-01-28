package com.runjian.parsing.vo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 心跳信息
 * @author Miracle
 * @date 2023/1/16 9:42
 */
@Data
public class HeartbeatDto {

    /**
     * 过期时间戳
     */
    @NotBlank(message = "过期时间戳不能为空")
    @Pattern(regexp = "^[1-9]\\d*$", message = "非法过期时间")
    private String outTime;
}
