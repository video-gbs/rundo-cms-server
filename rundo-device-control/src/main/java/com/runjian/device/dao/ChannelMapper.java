package com.runjian.device.dao;

import com.runjian.device.entity.ChannelInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 通道数据库操作类
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Mapper
@Repository
public interface ChannelMapper {

    String CHANNEL_TABLE_NAME = "rundo_channel";

    @Insert({" <script> " +
            " INSERT INTO " + CHANNEL_TABLE_NAME + "(id, device_id, sign_state, online_state, channel_type, stream_mode, update_time, create_time) values " +
            " <foreach collection='saveList' item='item' separator=','>(#{id.id}, #{item.deviceId}, #{item.signState}, #{item.onlineState}, #{item.channel_type}, #{item.streamMode}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    void batchSave(List<ChannelInfo> saveList);

    @Select(" SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE id = #{channelId} ")
    Optional<ChannelInfo> selectById(Long channelId);

    @Update(" UPDATE "  + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " sign_state = #{signState} " +
            " WHERE id = #{id} ")
    void updateSignState(ChannelInfo channelInfo);

    @Update(" UPDATE "  + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " online_state = #{online_state} " +
            " WHERE device_id = #{deviceId} ")
    void updateOnlineStateByDeviceId(Long deviceId, Integer onlineState);

    @Delete(" DELETE FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id = #{deviceId} ")
    void deleteByDeviceId(Long deviceId);

    @Select(" SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id = #{deviceId} ")
    List<ChannelInfo> selectByDeviceId(Long deviceId);

    @Update(" <script> " +
            " <foreach collection='channelInfoList' item='item' separator=';'> " +
            " UPDATE " + CHANNEL_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , sign_state = #{item.sign_state} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script>")
    void batchUpdateSignState(List<ChannelInfo> channelInfoList);
}
