package com.runjian.parsing.dao;

import com.runjian.parsing.entity.StreamTaskInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
            " (#{dispatchId}, #{channelId}, #{streamId}, #{msgType}, #{state}, #{detail}, #{mqId}, #{updateTime}, #{createTime})")
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
}
