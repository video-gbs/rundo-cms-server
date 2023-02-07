package com.runjian.device.expansion.vo.feign.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenjialing
 */
@Data
public class VideoAreaResp {

    @ApiModelProperty("安防区域名称")
    private String areaName;

    @ApiModelProperty("所属安防区域")
    private String areaNames;

    @ApiModelProperty("主键ID")
    private Long id;




}
