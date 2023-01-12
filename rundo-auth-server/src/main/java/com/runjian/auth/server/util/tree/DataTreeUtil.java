package com.runjian.auth.server.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName DataTreeUtil
 * @Description 构造树形结构工具
 * @date 2023-01-10 周二 17:18
 */
public class DataTreeUtil {

    public static <T extends DataTree<T>> List<T> buiidTree(List<T> paramList, Long rootNodeId) {
        List<T> returnList = new ArrayList<T>();
        for (T node : paramList) {//查找根节点
            if (node.getId().equals(rootNodeId)) {
                returnList.add(node);
            }
        }
        //递归为children赋值
        for (T entry : paramList) {
            toTreeChildren(returnList, entry);
        }
        return returnList;
    }

    private static <T extends DataTree<T>> void toTreeChildren(List<T> returnList, T entry) {
        for (T node : returnList) {
            if (entry.getParentId().equals(node.getId())) {
                if (node.getChildren() == null) {
                    node.setChildren(new ArrayList<T>());
                }
                node.getChildren().add(entry);
            }
            if (node.getChildren() != null) {
                toTreeChildren(node.getChildren(), entry);
            }
        }
    }
}
