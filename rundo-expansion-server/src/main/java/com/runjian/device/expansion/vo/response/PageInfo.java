package com.runjian.device.expansion.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author chenjialing
 */
@Data
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    protected long total;
    protected List<T> list;
    private int pageNum;
    private int pageSize;
    private int size;
    private long startRow;
    private long endRow;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int[] navigatepageNums;
    private int navigateFirstPage;
    private int navigateLastPage;

}
