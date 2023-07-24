package com.runjian.device.expansion.vo.feign.response;

import com.runjian.common.constant.MarkConstant;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/13 10:06
 */
@Data
public abstract class AbstractTreeInfo {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 层级
     */
    private String level;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 孩子节点
     */
    private List<? extends AbstractTreeInfo> childList;

    /**
     * 获取子节点
     * @param treeInfoList 树信息列表
     * @param level 层级
     * @return
     */
    public List<? extends AbstractTreeInfo> recursionData(List<? extends AbstractTreeInfo> treeInfoList, String level){
        List<? extends AbstractTreeInfo> next = treeInfoList.stream().filter(menuInfo -> menuInfo.getLevel().equals(level)).collect(Collectors.toList());
        for (AbstractTreeInfo treeNode : next){
            List<? extends AbstractTreeInfo> menuTreeRspList = treeInfoList.stream()
                    .filter(node -> node.getLevel().startsWith(treeNode.getLevel() + MarkConstant.MARK_SPLIT_RAIL + treeNode.getId())).collect(Collectors.toList());
            treeNode.setChildList(recursionData(menuTreeRspList, treeNode.getLevel() + MarkConstant.MARK_SPLIT_RAIL + treeNode.getId()));
        }
        return next.stream().sorted(Comparator.comparing(AbstractTreeInfo::getSort)).collect(Collectors.toList());
    }
}
