package com.runjian.stream.dao;

import com.runjian.stream.entity.StreamInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/3 10:22
 */
@Mapper
@Repository
public interface StreamMapper {

    String STREAM_TABLE_NAME = "rundo_stream";

    @Select(" SELECT * FROM " + STREAM_TABLE_NAME +
            " WHERE stream_id = #{streamId} ")
    Optional<StreamInfo> selectByStreamId(String streamId);

    @Delete(" DELETE FROM " + STREAM_TABLE_NAME +
            " WHERE stream_id = #{streamId} ")
    void deleteByStreamId(String streamId);

    @Update(" UPDATE " + STREAM_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , stream_state = #{streamState} " +
            " WHERE id = #{id} ")
    void updateStreamState(StreamInfo streamInfo);

    @Insert(" INSERT INTO " + STREAM_TABLE_NAME +
            " (gateway_id, channel_id, stream_id, dispatch_id, playType, record_state, auto_close_state, stream_state, update_time, create_time) " +
            " VALUES " +
            " (#{gatewayId}, #{channelId}, #{streamId}, #{dispatchId}, #{playType}, #{recordState}, #{autoCloseState}, #{streamState}, #{updateTime}, #{createTime})")
    void save(StreamInfo streamInfo);

    @Select(" SELECT * FROM " + STREAM_TABLE_NAME +
            " WHERE channel_id = #{channelId} ")
    List<StreamInfo> selectByChannelId(Long channelId);

    @Update(" UPDATE " + STREAM_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , record_state = #{recordState} " +
            " WHERE id = #{id} ")
    void updateRecordState(StreamInfo streamInfo);
}
