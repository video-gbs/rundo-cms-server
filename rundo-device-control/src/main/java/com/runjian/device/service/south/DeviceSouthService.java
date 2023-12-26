package com.runjian.device.service.south;


import com.runjian.device.vo.request.PostChannelDetailReq;
import com.runjian.device.vo.request.PostDeviceSignInReq;
import com.runjian.device.vo.request.PostNodeReq;
import com.runjian.device.vo.response.ChannelDetailRsp;

import java.util.List;

/**
 * 设备南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
public interface DeviceSouthService {

    /**
     * 设备添加注册
     * @param id 设备id
     * @param gatewayId 网关id
     * @param originId 原始id
     * @param onlineState 在线状态
     * @param deviceType 设备类型
     * @param ip ip地址
     * @param port 端口
     */
    void signIn(Long id, Long gatewayId, String originId, Integer onlineState, Integer deviceType, String ip, String port,
                String name, String manufacturer, String model, String firmware, Integer ptzType, String username, String password);

    /**
     * 批量注册
     * @param req
     */
    void signInBatch(List<PostDeviceSignInReq> req);


    /**
     * 节点同步
     * @param req
     */
    void nodeSync(Long deviceId, List<PostNodeReq> req);

    /**
     * 通道订阅
     * @param deviceId
     * @param channelDetailReqList
     */
    void channelSubscribe(Long deviceId, List<PostChannelDetailReq> channelDetailReqList);

    /**
     * 节点订阅
     * @param deviceId
     * @param subscribeType
     * @param req
     */
    void nodeSubscribe(Long deviceId, Integer subscribeType, List<PostNodeReq> req);

}
