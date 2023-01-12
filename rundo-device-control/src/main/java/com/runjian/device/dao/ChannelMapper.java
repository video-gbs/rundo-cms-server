package com.runjian.device.dao;

import com.runjian.device.entity.ChannelInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 通道数据库操作类
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Mapper
@Repository
public interface ChannelMapper {

    String CHANNEL_TABLE_NAME = "rundo_channel";

    @Insert({" <script> " +
            " INSERT INTO " + CHANNEL_TABLE_NAME + "(dcId, type, ip, port, name, manufacturer, model, firmware, ptz_type, username, password, update_time, create_time) values " +
            " <foreach collection='detailSaveList' item='item' separator=','>(#{item.dcId}, #{item.type}, #{item.ip}, #{item.port}, #{item.name}, #{item.manufacturer}, #{item.model}, #{item.firmware}, #{item.ptzType}, #{item.username}, #{item.password}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    void batchSave(List<ChannelInfo> saveList);

    @Select(" SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<ChannelInfo> selectById(Long channelId);

    @Update(" UPDATE "  + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " sign_state = #{signState} " +
            " WHERE id = #{id} ")
    void updateSignState(ChannelInfo channelInfo);

    @Update(" UPDATE "  + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " online_state = #{online_state} " +
            " WHERE device_id = #{deviceId} ")
    void updateOnlineStateByDeviceId(Long deviceId, Integer onlineState);
}
