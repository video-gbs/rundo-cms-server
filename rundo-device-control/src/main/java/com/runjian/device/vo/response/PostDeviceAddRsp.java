package com.runjian.device.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miracle
 * @date 2023/2/20 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDeviceAddRsp {

    /**
     * 设备id
     */
    private Long id;

    /**
     * 是否成功注册
     */
    private Integer onlineState;
}
