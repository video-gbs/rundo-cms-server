package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/12/15 15:39
 */
@Data
public class PostNodeSubscribeReq {

    /**
     * 设备id
     */
    @NotNull(message = "设备id不能为空")
    private Long deviceId;

    /**
     * 订阅类型
     */
    @NotNull(message = "订阅类型不能为空")
    @Range(min = 0, max = 5, message = "订阅类型不正确")
    private Integer subscribeType;

    /**
     * 节点数据
     */
    @NotNull(message = "节点数据不能为空")
    @Size(min = 1, message = "节点数据不能为空")
    private List<PostNodeReq> postNodeReqList;
}
