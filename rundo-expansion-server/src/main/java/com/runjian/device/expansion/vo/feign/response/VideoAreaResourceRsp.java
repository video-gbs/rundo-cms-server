package com.runjian.device.expansion.vo.feign.response;

import com.runjian.device.expansion.vo.feign.request.GetCatalogueResourceRsp;
import lombok.Data;

import java.util.List;

/**
 * @author chenjialing
 */
@Data
public class VideoAreaResourceRsp {

    List<GetCatalogueResourceRsp> dataList;

    List<Long> channelData;

}
