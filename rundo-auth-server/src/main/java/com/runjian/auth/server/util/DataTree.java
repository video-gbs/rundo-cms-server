package com.runjian.auth.server.util;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName DataTree
 * @Description 树形结构数据工具
 * @date 2023-01-10 周二 17:12
 */
public interface DataTree<T> {

    public Long getId();

    public Long getParentId();

    public void setChildren(List<T> children);

    public List<T> getChildren();

}
