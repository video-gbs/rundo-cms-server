package com.runjian.device.expansion.vo.response;

import lombok.Data;

import java.util.List;

/**
 * @author chenjialing
 */
@Data
public class PageResp<T> {
    protected long total;
    protected long size;
    protected long current;

    protected List<T> records;
}
