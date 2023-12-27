package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/12/15 15:26
 */
@Data
public class PostNodeSyncReq {

    /**
     * 设备id
     */
    @NotNull(message = "设备id不能为空")
    @Range(min = 1, message = "设备id必须大于0")
    private Long deviceId;

    /**
     * 节点id
     */
    @NotNull(message = "节点数据不能为空")
    @Size(min = 1, message = "节点数据必须大于0")
    private List<PostNodeReq> nodeReqList;
}
