package com.runjian.parsing.dao;

import com.runjian.parsing.entity.ChannelInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface ChannelMapper {

    String CHANNEL_TABLE_NAME = "rundo_channel";

    @Select(" SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE id = #{channelId} ")
    Optional<ChannelInfo> selectById(Long channelId);

    @Delete(" DELETE FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id = #{deviceId} ")
    void deleteByDeviceId(Long deviceId);

    @Select(" <script>" +
            " SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id = #{deviceId} AND origin_id = #{originId} " +
            " </script>")
    Optional<ChannelInfo> selectByDeviceIdAndOriginId(Long deviceId, String originId);

    @Insert(" INSERT INTO " + CHANNEL_TABLE_NAME +
            " (device_id, origin_id, update_time, create_time) " +
            " VALUES " +
            " (#{deviceId}, #{originId}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(ChannelInfo channelInfo);

    @Insert({" <script> " +
            " INSERT INTO " + CHANNEL_TABLE_NAME + "(device_id, origin_id, update_time, create_time) values " +
            " <foreach collection='channelInfoList' item='item' separator=','>(#{item.deviceId}, #{item.originId}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    @Options(useGeneratedKeys = true, keyProperty = "channelInfoList.id", keyColumn = "id")
    void batchSave(List<ChannelInfo> channelInfoList);

    @Delete(" DELETE FROM " + CHANNEL_TABLE_NAME +
            " WHERE id = #{channelId} ")
    void deleteById(Long channelId);
}
