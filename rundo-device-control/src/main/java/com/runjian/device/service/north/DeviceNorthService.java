package com.runjian.device.service.north;


import com.github.pagehelper.PageInfo;
import com.runjian.device.vo.response.DeviceSyncRsp;
import com.runjian.device.vo.response.GetDevicePageRsp;
import com.runjian.device.vo.response.GetNodeRsp;
import com.runjian.device.vo.response.PostDeviceAddRsp;

import java.util.List;

/**
 * 设备北向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
public interface DeviceNorthService {

    /**
     * 获取设备注册列表
     */
    PageInfo<GetDevicePageRsp> getDeviceByPage(int page, int num, Integer signState, String deviceName, String ip);

    /**
     * 获取下级平台注册列表
     */
    PageInfo<GetDevicePageRsp> getPlatformByPage(int page, int num, String deviceName, String ip);

    /**
     * 获取节点数据
     * @param deviceId 设备id
     * @return
     */
    List<GetNodeRsp> getNodeRsp(Long deviceId);

    /**
     * 获取设备详情
     * @param deviceId 设备id
     * @return
     */
    GetDevicePageRsp getDeviceByDeviceId(Long deviceId);

    /**
     * 设备添加注册
     */
    PostDeviceAddRsp deviceAdd(String deviceId, Long gatewayId, Integer deviceType,
                               String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType, String username, String password);

    /**
     * 设备注册状态修改
     */
    void deviceSignSuccess(Long deviceId);

    /**
     * 设备同步
     */
    DeviceSyncRsp deviceSync(Long deviceId);

    /**
     * 软删除设备
     * @param deviceId
     */
    void deviceDeleteSoft(Long deviceId);

    /**
     * 硬删除设备
     */
    void deviceDeleteHard(Long deviceId);


    /**
     * 下级设备订阅
     * @param deviceId 设备id
     * @param isSubscribe 是否订阅
     */
    void deviceSubscribe(Long deviceId, Integer isSubscribe);


}
