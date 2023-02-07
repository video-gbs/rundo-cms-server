package com.runjian.parsing.dao;

import com.runjian.parsing.entity.ChannelInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

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
            " WHERE device_id = #{deviceId} ")
    Optional<ChannelInfo> selectById(Long id);

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

}
