package com.runjian.device.expansion.service;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResourceRsp;

import java.util.List;

public interface IBaseDeviceAndChannelService {


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
    void commonResourceBind(String resourceKey,String pResourceValue,Long resourceId,String resourceName);


    /**
     * 资源修改
     * @param videoAreaId
     * @param resourceId
     * @param resourceName
     */
    void commonResourceUpdate(String resourceKey, String resourceValue,String resourceName);
    /**
     * 资源移动
     * @param resourceId
     * @param pid


    /**
     * 资源移动目录
     * @param resourceKey
     * @param resourceValue
     * @param pResourceValue
     */
    void moveResourceByValue(String resourceKey,String resourceValue,String pResourceValue);

    /**
     * 根据资源value删除数据
     * @param resourceKey
     * @param resourceValue
     */
    void commonDeleteByResourceValue(String resourceKey, String resourceValue);
}
