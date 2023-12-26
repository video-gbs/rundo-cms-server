package com.runjian.cascade.service;

/**
 * @author Miracle
 * @date 2023/12/22 17:02
 */
public interface NodeService {

    /**
     * 获取全部节点数据
     */
    void getAllNode(String resourceKey);

    /**
     * 新增节点
     */
    void addNode(Long nodePid, String nodeName, String gbCode, Long resourceId);

    /**
     * 修改节点
     * @param nodeId
     * @param nodeName
     * @param gbCode
     * @param resourceId
     */
    void updateNode(Long nodeId, String nodeName, String gbCode, Long resourceId);

    /**
     * 删除节点
     * @param nodeId
     */
    void deleteNode(Long nodeId);

    /**
     * 移动节点
     * @param nodeId
     * @param newNodePid
     */
    void moveNode(Long nodeId, Long newNodePid);

    /**
     * 移动节点
     * @param nodeId
     * @param upOrDown
     */
    void sortNode(Long nodeId, Integer upOrDown);
}
