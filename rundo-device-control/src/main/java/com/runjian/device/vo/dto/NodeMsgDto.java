package com.runjian.device.vo.dto;

import com.runjian.device.entity.NodeInfo;
import jodd.bean.BeanUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Miracle
 * @date 2023/12/19 14:35
 */
@Data
public class NodeMsgDto {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 母节点id
     */
    private String parentId;

    /**
     * 原始id
     */
    private String originId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 1->ADD 2->DELETE 3->UPDATE
     */
    private Integer state;

    public static NodeMsgDto nodeInfoConvert(NodeInfo nodeInfo, Integer state) {
        NodeMsgDto nodeMsgDto = new NodeMsgDto();
        BeanUtils.copyProperties(nodeInfo, nodeMsgDto);
        nodeMsgDto.setState(state);
        return nodeMsgDto;
    }
}
