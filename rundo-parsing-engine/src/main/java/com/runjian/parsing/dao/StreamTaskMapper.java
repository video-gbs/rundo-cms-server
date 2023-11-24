package com.runjian.parsing.dao;

import com.runjian.parsing.entity.StreamTaskInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/10 11:14
 */
@Repository
@Mapper
public interface StreamTaskMapper {

    String STREAM_TASK_TABLE_NAME = "rundo_stream_task";

    @Insert(" INSERT INTO " + STREAM_TASK_TABLE_NAME +
            " (dispatch_id, channel_id, stream_id, mq_id, msg_type, state, detail, update_time, create_time) " +
            " VALUES " +
            " (#{dispatchId}, #{channelId}, #{streamId}, #{mqId}, #{msgType}, #{state}, #{detail}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(StreamTaskInfo streamTaskInfo);

    @Select(" SELECT * FROM " + STREAM_TASK_TABLE_NAME +
            " WHERE id = #{taskId} ")
    Optional<StreamTaskInfo> selectById(Long taskId);

    @Update(" <script> " +
            " UPDATE "  + STREAM_TASK_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " , state = #{taskState} " +
            " <if test='detail != null'>, detail = #{detail} </if> " +
            " WHERE id = #{taskId} " +
            " </script> ")
    void updateState(Long taskId, Integer taskState, String detail, LocalDateTime updateTime);

    @Select(" <script>" +
            " SELECT * FROM " + STREAM_TASK_TABLE_NAME +
            " WHERE state = #{state} " +
            " AND update_time &lt;= #{outTime} " +
            " </script>")
    List<StreamTaskInfo> selectByOutTimeTask(Integer state, LocalDateTime outTime);

    @Update(" <script> " +
            " <foreach collection='streamTaskInfoList' item='item' separator=';'> " +
            " UPDATE " + STREAM_TASK_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , state = #{item.state} " +
            " , detail = #{item.detail}" +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void updateAll(List<StreamTaskInfo> streamTaskInfoList);

    @Update(" <script> " +
            " <foreach collection='idList' item='item' separator=';'> " +
            " UPDATE " + STREAM_TASK_TABLE_NAME +
            " SET update_time = #{nowTime}  " +
            " , state = #{taskState} " +
            " , detail = #{detail}" +
            " WHERE id = #{item} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateState(List<Long> idList, Integer taskState, String detail, LocalDateTime nowTime);
}
