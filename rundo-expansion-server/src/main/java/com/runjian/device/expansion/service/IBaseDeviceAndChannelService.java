package com.runjian.device.expansion.service;

import com.runjian.device.expansion.vo.feign.response.VideoAreaResourceRsp;

import java.util.List;

public interface IBaseDeviceAndChannelService {

    /**
     * 删除设备
     * @param id
     */
    void removeDevice(Long id);

    /**
     * 软删除设备
     * @param id
     */
    void removeDeviceSoft(Long id);

    /**
     * 获取资源得id
     * @param videoAreaId
     * @param flag
     * @return
     */
    VideoAreaResourceRsp resourceIdList(Long videoAreaId, Boolean flag);

    /**
     * 资源绑定
     * @param videoAreaId
     * @param resourceId
     * @param resourceName
     */
    void commonResourceBind(Long videoAreaId,Long resourceId,String resourceName);

    /**
     * 资源移动
     * @param resourceId
     * @param pid
     */
    void commonResourceUpdate(Long resourceId,Long pid);
}
