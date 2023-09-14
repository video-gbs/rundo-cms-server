package com.runjian.alarm.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/14 11:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutDefenseReq {

    /**
     * 通道id列表
     */
    @NotNull(message =  "通道id列表不能为空")
    @Size(min = 1, message = "通道id列表不能为空")
    private List<Long> channelIdList;

    /**
     * 是否布防
     */
    @NotNull(message = "布防状态不能为空")
    private Boolean isDeploy;
}
