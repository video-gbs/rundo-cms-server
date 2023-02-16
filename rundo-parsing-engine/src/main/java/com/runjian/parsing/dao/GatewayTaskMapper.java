package com.runjian.parsing.dao;

import com.runjian.parsing.entity.GatewayTaskInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface GatewayTaskMapper {

    String GATEWAY_TASK_TABLE_NAME = "rundo_gateway_task";


    @Insert(" INSERT INTO " + GATEWAY_TASK_TABLE_NAME +
            " (gateway_id, device_id, channel_id, mq_id, msg_type, state, detail, update_time, create_time) " +
            " VALUES " +
            " (#{gatewayId}, #{deviceId}, #{channelId}, #{mqId}, #{msgType}, #{state}, #{detail}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(GatewayTaskInfo gatewayTaskInfo);

    @Select(" <script>" +
            " SELECT * FROM " + GATEWAY_TASK_TABLE_NAME +
            " WHERE id = #{taskId} " +
            " </script>")
    Optional<GatewayTaskInfo> selectById(Long taskId);

    @Update(value = {" <script> " +
            " UPDATE " + GATEWAY_TASK_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test='deviceId != null'>, device_id = #{deviceId} </if> " +
            " <if test='channelId != null'>, channel_id = #{channelId} </if> " +
            " <if test='msgType != null'>, msg_type = #{msgType} </if> " +
            " <if test='detail != null'>, detail = #{detail} </if> " +
            " WHERE id = #{id} "+
            " </script> "})
    void update(GatewayTaskInfo gatewayTaskInfo);

    @Update(" <script> " +
            " UPDATE "  + GATEWAY_TASK_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " , state = #{state} " +
            " <if test='detail != null'>, detail = #{detail} </if> " +
            " WHERE id = #{taskId} " +
            " </script> ")
    void updateState(Long taskId, Integer state, String detail, LocalDateTime updateTime);

}
