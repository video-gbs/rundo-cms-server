package com.runjian.parsing.vo.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/2/9 15:29
 */
@Data
public class StreamConvertDto {

    /**
     * 流id
     */
    private String streamId;

    /**
     * 其他数据
     */
    private Map<String, Object> dataMap;
}
