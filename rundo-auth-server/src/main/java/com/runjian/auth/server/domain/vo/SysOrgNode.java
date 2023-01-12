package com.runjian.auth.server.domain.vo;

import com.runjian.auth.server.entity.system.SysOrg;
import com.runjian.auth.server.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysOrgNode
 * @Description 树形
 * @date 2023-01-12 周四 11:46
 */
public class SysOrgNode extends SysOrg implements DataTree<SysOrgNode> {

    private List<SysOrgNode> children;

    @Override
    public Long getParentId() {
        return super.getOrgPid();
    }

    @Override
    public void setChildren(List<SysOrgNode> children) {
        this.children = children;
    }

    @Override
    public List<SysOrgNode> getChildren() {
        return this.children;
    }
}
