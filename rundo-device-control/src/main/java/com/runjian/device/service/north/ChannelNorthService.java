package com.runjian.device.service.north;

import com.github.pagehelper.PageInfo;
import com.runjian.device.vo.response.GetChannelByPageRsp;
import com.runjian.device.vo.response.VideoRecordRsp;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.VideoPlayRsp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/1/9 9:53
 */
public interface ChannelNorthService {

    /**
     * 获取成功注册的设备的通道
     */
    PageInfo<GetChannelByPageRsp> getChannelByPage(int page, int num, String nameOrOriginId);

    /**
     * 通道同步
     * @param deviceId
     * @return
     */
    ChannelSyncRsp channelSync(Long deviceId);

    /**
     * 通道注册状态转为成功
     * @param channelId 通道Id
     */
    void channelSignSuccess(List<Long> channelId);

    /**
     * 删除设备Id
     * @param deviceId 设备id
     */
    void channelDeleteByDeviceId(Long deviceId, Boolean isDeleteData);

    /**
     * 点播
     * @param chId 通道id
     */
    VideoPlayRsp channelPlay(Long chId, Boolean enableAudio, Boolean ssrcCheck);

    /**
     * 获取录像数据
     * @param chId 通道id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    VideoRecordRsp channelRecord(Long chId, LocalDateTime startTime, LocalDateTime endTime);



    /**
     * 回放
     * @param chId 通道id
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    VideoPlayRsp channelPlayback(Long chId, Boolean enableAudio, Boolean ssrcCheck, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 云台控制
     * @param chId 通道id
     * @param commandCode 控制指令,允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop
     * @param horizonSpeed 水平速度
     * @param verticalSpeed 垂直速度
     * @param zoomSpeed 缩放速度
     */
    void channelPtzControl(Long chId, Integer commandCode, Integer horizonSpeed, Integer verticalSpeed, Integer zoomSpeed, Integer totalSpeed);
}
