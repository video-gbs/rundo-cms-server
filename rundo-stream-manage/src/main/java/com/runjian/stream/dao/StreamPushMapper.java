package com.runjian.stream.dao;

import com.runjian.stream.entity.StreamPushInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/12/7 9:21
 */

@Mapper
@Repository
public interface StreamPushMapper {

    String STREAM_PUSH_TABLE_NAME = "rundo_stream_push";

    @Select(" SELECT * FROM " + STREAM_PUSH_TABLE_NAME +
            " WHERE state = #{state} ")
    List<StreamPushInfo> selectByState(Integer state);

    @Select(" SELECT * FROM " + STREAM_PUSH_TABLE_NAME +
            " WHERE id = #{streamPushId} ")
    Optional<StreamPushInfo> selectById(Long streamPushId);

    @Delete(" DELETE FROM " + STREAM_PUSH_TABLE_NAME +
            " WHERE id = #{streamPushId} ")
    void deleteById(Long streamPushId);

    @Update(" UPDATE " + STREAM_PUSH_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , stream_id = #{streamId} " +
            " , state = #{state} " +
            " WHERE id = #{id} ")
    void update(StreamPushInfo streamPushInfo);

    @Select(" SELECT * FROM " + STREAM_PUSH_TABLE_NAME)
    List<StreamPushInfo> selectAll();

    @Insert(" INSERT INTO " + STREAM_PUSH_TABLE_NAME +
            " (channel_id, stream_id, ssrc, dst_url, dst_port, auto_close_state, stream_state, update_time, create_time) " +
            " VALUES " +
            " (#{channelId}, #{streamId}, #{ssrc}, #{dstUrl}, #{dstPort}, #{autoCloseState}, #{streamState}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(StreamPushInfo streamPushInfo);
}
