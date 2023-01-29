package com.runjian.parsing.dao;

import com.runjian.parsing.entity.TaskInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface TaskMapper {

    String TASK_TABLE_NAME = "rundo_task";


    @Insert(" INSERT INTO " + TASK_TABLE_NAME +
            " (gateway_id, device_id, channel_id, client_msg_id, mq_id, msg_type, state, desc, update_time, create_time) " +
            " VALUES " +
            " (#{gatewayId}, #{deviceId}, #{channelId}, #{clientMsgId}, #{mqId}, #{msgType}, #{state}, #{desc}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(TaskInfo taskInfo);

    @Select(" <script>" +
            " SELECT * FROM " + TASK_TABLE_NAME +
            " WHERE id = #{taskId} " +
            " </script>")
    Optional<TaskInfo> selectById(Long taskId);

    @Update(value = {" <script> " +
            " UPDATE " + TASK_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test='deviceId != null'>, device_id = #{deviceId} </if> " +
            " <if test='channelId != null'>, channel_id = #{channelId} </if> " +
            " <if test='msgType != null'>, msg_type = #{msgType} </if> " +
            " <if test='desc != null'>, desc = #{desc} </if> " +
            " WHERE id = #{id} "+
            " </script> "})
    void update(TaskInfo taskInfo);

    @Update(" <script> " +
            " UPDATE "  + TASK_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " , state = #{state} " +
            " <if test='detail != null'>, desc = #{desc} </if> " +
            " WHERE id = #{taskId} " +
            " </script> ")
    void updateState(Long taskId, Integer state, String desc, LocalDateTime now);
}
