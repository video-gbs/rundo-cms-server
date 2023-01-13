package com.runjian.auth.server.domain.vo;

import com.runjian.auth.server.entity.video.VideoArae;
import com.runjian.auth.server.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AreaNode
 * @Description 安保区域树形
 * @date 2023-01-13 周五 9:54
 */
public class AreaNode extends VideoArae implements DataTree<AreaNode> {

    private List<AreaNode> children;

    @Override
    public Long getParentId() {
        return super.getAreaPid();
    }

    @Override
    public void setChildren(List<AreaNode> children) {
        this.children = children;
    }

    @Override
    public List<AreaNode> getChildren() {
        return this.children;
    }
}
