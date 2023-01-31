package com.runjian.auth.server.domain.vo.tree;

import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysApiInfoTree
 * @Description 接口树
 * @date 2023-01-31 周二 16:06
 */
public class SysApiInfoTree extends SysApiInfoVO implements DataTree<SysApiInfoTree> {

    List<SysApiInfoTree> children;

    @Override
    public Long getParentId() {
        return super.getApiPid();
    }

    @Override
    public void setChildren(List<SysApiInfoTree> children) {
        this.children = children;
    }

    @Override
    public List<SysApiInfoTree> getChildren() {
        return this.children;
    }
}
