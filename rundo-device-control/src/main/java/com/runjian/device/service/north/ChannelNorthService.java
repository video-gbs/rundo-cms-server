package com.runjian.device.service.north;

import com.github.pagehelper.PageInfo;
import com.runjian.device.vo.response.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     * 通道删除
     * @param channelId 通道id
     */
    void channelDeleteByChannelId(List<Long> channelId);

    /**
     * 删除设备Id
     * @param deviceId 设备id
     */
    void channelDeleteByDeviceId(Long deviceId, Boolean isDeleteData);

    /**
     * 获取录像数据
     * @param chId 通道id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    VideoRecordRsp channelRecord(Long chId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 云台控制
     * @param channelId 通道id
     * @param cmdCode 控制指令
     * @param cmdValue 通用值
     * @param valueMap 参数
     */
    void channelPtzControl(Long channelId, Integer cmdCode, Integer cmdValue, Map<String, Object> valueMap);

    /**
     * 预置位查询
     * @param channelId 通道id
     */
    List<PtzPresetRsp> channelPtzPresetGet(Long channelId);

    /**
     * 3d缩放扩大
     * @param channelId 通道id
     * @param dragType 放大-1 缩小-2
     * @param length 拉宽长度
     * @param width 拉宽宽度
     * @param midPointX 拉框中心的横轴坐标像素值
     * @param midPointY 拉框中心的纵轴坐标像素值
     * @param lengthX 拉框长度像素值
     * @param lengthY 拉框宽度像素值
     */
    void channelPtz3d(Long channelId, Integer dragType, Integer length, Integer width, Integer midPointX, Integer midPointY, Integer lengthX, Integer lengthY);
}
