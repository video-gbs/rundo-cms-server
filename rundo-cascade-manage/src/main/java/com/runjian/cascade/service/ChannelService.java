package com.runjian.cascade.service;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/12/25 16:12
 */
public interface ChannelService {

    /**
     * 获取未添加的通道
     * @param page
     * @param num
     * @param channelName
     * @param gbCode
     * @param onlineState
     */
    void getNotAddChannel(int page, int num, String channelName, String gbCode, Integer onlineState);

    /**
     * 获取已添加的通道
     * @param page
     * @param num
     * @param channelName
     * @param originGbCode
     * @param gbCode
     */
    void getChannel(int page, int num, String channelName, String originGbCode, String gbCode);


    /**
     * 批量添加数据
     * @param nodeId
     * @param channelIds
     */
    void batchAdd(Long nodeId, Set<Long> channelIds);


    /**
     * 修改国标编码
     * @param channelRelId
     * @param gbCode
     */
    void updateChannel(Long channelRelId, String gbCode);

    /**
     * 批量删除
     * @param channelRelIds
     */
    void batchDeleteChannel(Set<Long> channelRelIds);

    /**
     * 批量移动
     * @param nodeId
     * @param channelRelIds
     */
    void batchMoveChannel(Long nodeId, Set<Long> channelRelIds);
}
