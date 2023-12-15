package com.runjian.device.vo.request;

import com.runjian.device.entity.NodeInfo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/14 16:10
 */
@Data
public class PostNodeReq {

    /**
     * 节点id
     */
    @NotBlank(message = "节点id不能为空")
    private String nodeId;

    /**
     * 节点名称
     */
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;

    /**
     * 父节点id
     */
    private String parentId;

    public NodeInfo toNodeInfo(Long deviceId, LocalDateTime nowTime) {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setDeviceId(deviceId);
        nodeInfo.setOriginId(this.nodeId);
        nodeInfo.setParentId(this.parentId);
        nodeInfo.setNodeName(this.nodeName);
        nodeInfo.setCreateTime(nowTime);
        nodeInfo.setUpdateTime(nowTime);
        return nodeInfo;
    }
}
