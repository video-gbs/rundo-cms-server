package com.runjian.device.dao;

import com.runjian.device.entity.MessageInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/5/16 14:32
 */
@Mapper
@Repository
public interface MessageMapper {

    String MESSAGE_TABLE_NAME = "rundo_message";

    @Select(" SELECT * FROM " + MESSAGE_TABLE_NAME +
            " WHERE msg_handle = #{msgHandle} ")
    Optional<MessageInfo> selectByMsgHandle(String msgHandle);

    @Insert(" <script> " +
            " INSERT INTO " + MESSAGE_TABLE_NAME + "(service_name, msg_type, msg_handle, msg_lock, create_time) values " +
            " <foreach collection='saveList' item='item' separator=','>(#{item.serviceName}, #{item.msgType}, #{item.msgHandle}, #{item.msgLock}, #{item.createTime})</foreach> " +
            " </script> ")
    void batchSave(List<MessageInfo> saveList);

    @Update(" <script> " +
            " <foreach collection='updateList' item='item' separator=';'> " +
            " UPDATE " + MESSAGE_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , online_state = #{onlineState} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdate(List<MessageInfo> updateList);

    @Delete(" <script> " +
            " DELETE FROM " + MESSAGE_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='delIdList'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " </script> ")
    void batchDelete(List<Long> delIdList);

    @Select(" SELECT * FROM " + MESSAGE_TABLE_NAME +
            " WHERE msg_type = #{msgType} ")
    List<MessageInfo> selectAllByMsgType(Integer msgType);
}
