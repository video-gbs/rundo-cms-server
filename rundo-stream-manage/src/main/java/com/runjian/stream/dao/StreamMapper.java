package com.runjian.stream.dao;

import com.runjian.stream.entity.StreamInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            " (gateway_id, channel_id, stream_id, dispatch_id, play_type, record_state, auto_close_state, stream_state, update_time, create_time) " +
            " VALUES " +
            " (#{gatewayId}, #{channelId}, #{streamId}, #{dispatchId}, #{playType}, #{recordState}, #{autoCloseState}, #{streamState}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(StreamInfo streamInfo);

    @Select(" SELECT * FROM " + STREAM_TABLE_NAME +
            " WHERE channel_id = #{channelId} ")
    List<StreamInfo> selectByChannelId(Long channelId);

    @Update(" UPDATE " + STREAM_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , record_state = #{recordState} " +
            " WHERE id = #{id} ")
    void updateRecordState(StreamInfo streamInfo);

    @Update(" UPDATE " + STREAM_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , record_state = #{recordState} " +
            " , auto_close_state = #{autoCloseState} " +
            " WHERE id = #{id} ")
    void updateRecordAndAutoCloseState(StreamInfo streamInfo);

    @Select(" SELECT * FROM " + STREAM_TABLE_NAME +
            " WHERE stream_state = #{streamState} ")
    List<StreamInfo> selectByStreamState(Integer streamState);

    @Select(" SELECT * FROM " + STREAM_TABLE_NAME +
            " WHERE stream_state = #{streamState} ")
    List<Long> selectIdByStreamState(Integer streamState);

    @Delete(" <script> " +
            " DELETE FROM " + STREAM_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='idList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void deleteByIds(List<Long> idList);

    @Delete(" <script> " +
            " DELETE FROM " + STREAM_TABLE_NAME +
            " WHERE stream_id IN " +
            " <foreach collection='streamIdList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void deleteByStreamIds(List<String> streamIdList);

    @Select(" SELECT * FROM " + STREAM_TABLE_NAME +
            " WHERE record_state = #{recordState} AND " +
            " stream_state = #{streamState} ")
    List<StreamInfo> selectByRecordStateAndStreamState(Integer recordState, Integer streamState);

    @Update(" <script> " +
            " <foreach collection='noRecordIds' item='item' separator=';'> " +
            " UPDATE " + STREAM_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , record_state = #{recordState} " +
            " WHERE id = #{item} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateRecordState(List<Long> noRecordIds, Integer recordState, LocalDateTime updateTime);


    @Select(" <script> " +
            " SELECT * FROM " + STREAM_TABLE_NAME +
            " WHERE stream_id IN " +
            " <foreach collection='streamIdList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " <if test=\"recordState != null\" > AND record_state = #{recordState} </if> " +
            " <if test=\"streamState != null\" > AND stream_state = #{streamState} </if> " +
            " </script> ")
    List<StreamInfo> selectByStreamIdsAndRecordStateAndStreamState(List<String> streamIdList, Integer recordState, Integer streamState);
}
