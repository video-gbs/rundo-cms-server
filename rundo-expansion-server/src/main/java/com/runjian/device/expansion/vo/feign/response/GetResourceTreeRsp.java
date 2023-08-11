package com.runjian.device.expansion.vo.feign.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 资源树返回体
 * @author Miracle
 * @date 2023/6/13 9:42
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class GetResourceTreeRsp extends AbstractTreeInfo {

    /**
     * 资源父id
     */
    private Long resourcePid;

    /**
     * 资源类型 1-目录 2-资源
     */
    private Integer resourceType;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源key
     */
    private String resourceKey;

    /**
     * 资源value
     */
    private String resourceValue;

    /**
     * 层级
     */
    private String level;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
