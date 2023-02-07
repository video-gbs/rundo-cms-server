package com.runjian.auth.server.domain.vo.tree;

import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysMenuTree
 * @Description 菜单树
 * @date 2023-01-31 周二 15:46
 */
public class SysMenuInfoTree extends SysMenuInfoVO implements DataTree<SysMenuInfoTree> {

    List<SysMenuInfoTree> children;

    @Override
    public Long getParentId() {
        return super.getMenuPid();
    }

    @Override
    public void setChildren(List<SysMenuInfoTree> children) {
        this.children = children;
    }

    @Override
    public List<SysMenuInfoTree> getChildren() {
        return this.children;
    }
}
