package com.runjian.device.expansion.vo.feign.response;

import lombok.Data;

import java.util.List;

/**
 * @author chenjialing
 */
@Data
public class PageListResp<T> {
    protected long total;
    protected long size;
    protected long current;

    protected List<T> list;
}
