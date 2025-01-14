package com.runjian.device.dao;

import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.vo.response.GetChannelByPageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            " INSERT INTO " + CHANNEL_TABLE_NAME + "(id, device_id, node_origin_id, sign_state, online_state, channel_type, update_time, create_time) values " +
            " <foreach collection='saveList' item='item' separator=','>(#{item.id}, #{item.deviceId}, #{item.node_origin_id}, #{item.signState}, #{item.onlineState}, #{item.channelType}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    void batchSave(List<ChannelInfo> saveList);

    @Select(" SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE id = #{channelId} ")
    Optional<ChannelInfo> selectById(Long channelId);

    @Update(" UPDATE "  + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " online_state = #{onlineState} " +
            " WHERE device_id = #{deviceId} ")
    void updateOnlineStateByDeviceId(Long deviceId, Integer onlineState, LocalDateTime updateTime);

    @Delete(" DELETE FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id = #{deviceId} ")
    void deleteByDeviceId(Long deviceId);

    @Select(" SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id = #{deviceId} ")
    List<ChannelInfo> selectByDeviceId(Long deviceId);

    @Update(" <script> " +
            " <foreach collection='channelInfoList' item='item' separator=';'> " +
            " UPDATE " + CHANNEL_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , sign_state = #{item.signState} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateSignState(List<ChannelInfo> channelInfoList);

    @Update(" <script> " +
            " <foreach collection='channelInfoList' item='item' separator=';'> " +
            " UPDATE " + CHANNEL_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , online_state = #{item.onlineState} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateOnlineState(List<ChannelInfo> channelInfoList);

    @Update(" <script> " +
            " <foreach collection='channelIds' item='item' separator=';'> " +
            " UPDATE " + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , online_state = #{onlineState} " +
            " WHERE id = #{item} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateOnlineStateByIds(List<Long> channelIds, Integer onlineState, LocalDateTime updateTime);

    @Select(" <script> " +
            " SELECT ch.id AS channelId, ch.device_id, dt.name AS channelName, ch.sign_state, ch.online_state, ch.create_time, " +
            " dt.origin_id, dt.ip, dt.port, dt.manufacturer, dt.model, dt.firmware, dt.ptz_type, dt.username, dt.password  FROM " + CHANNEL_TABLE_NAME + " ch " +
            " LEFT JOIN " + DetailMapper.DETAIL_TABLE_NAME + " dt ON ch.id = dt.dc_id AND type = 2 " +
            " WHERE ch.sign_state != 0 " +
            " AND ch.device_id IN "  +
            " <foreach collection='deviceIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " <if test=\"nameOrOriginId != null\" >  AND (dt.name LIKE CONCAT('%', #{nameOrOriginId}, '%')  OR dt.origin_id LIKE CONCAT('%', #{nameOrOriginId}, '%')) </if> " +
            " ORDER BY ch.create_time desc " +
            " </script> ")
    List<GetChannelByPageRsp> selectByPage(List<Long> deviceIds, String nameOrOriginId);


    @Select(" <script> " +
            " SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='channelIdList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<ChannelInfo> selectByIds(List<Long> channelIdList);

    @Select(value = {" <script> " +
            " SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id IN" +
            " <foreach collection='deviceIds'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " AND online_state = #{onlineState} " +
            " </script> "})
    List<ChannelInfo> selectByDeviceIdsAndOnlineState(List<Long> deviceIds, Integer onlineState);

    @Update(" <script> " +
            " UPDATE " + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , online_state = #{onlineState} " +
            " WHERE device_id IN "+
            " <foreach collection='deviceIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void batchUpdateOnlineStateByDeviceIds(List<Long> deviceIds, Integer onlineState, LocalDateTime updateTime);

    @Select(value = {" <script> " +
            " SELECT * FROM " + CHANNEL_TABLE_NAME +
            " WHERE device_id = #{deviceId}" +
            " AND sign_state = #{signState} " +
            " </script> "})
    List<ChannelInfo> selectByDeviceIdAndSignState(Long deviceId, Integer signState);


    @Delete(" <script> " +
            " DELETE FROM " + CHANNEL_TABLE_NAME +
            " WHERE id = #{channelId} " +
            " </script>")
    void deleteById(Long channelId);

    @Update(" <script> " +
            " UPDATE " + CHANNEL_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , sign_state = #{signState} " +
            " WHERE id = #{id} "+
            " </script> ")
    void updateSignState(ChannelInfo channelInfo);

    @Update(" <script> " +
            " <foreach collection='channelInfoList' item='item' separator=';'> " +
            " UPDATE " + CHANNEL_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , online_state = #{item.onlineState} " +
            " , channel_type = #{item.channelType} " +
            " , node_origin_id = #{item.nodeOriginId}" +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdate(List<ChannelInfo> updateChannelInfoList);

    @Select(" <script> " +
            " SELECT ch.id AS channelId, ch.device_id, dt.name AS channelName, ch.node_origin_id, ch.sign_state, ch.online_state, ch.create_time, " +
            " dt.origin_id, dt.ip, dt.port, dt.manufacturer, dt.model, dt.firmware, dt.ptz_type, dt.username, dt.password  FROM " + CHANNEL_TABLE_NAME + " ch " +
            " LEFT JOIN " + DetailMapper.DETAIL_TABLE_NAME + " dt ON ch.id = dt.dc_id AND type = 2 " +
            " WHERE ch.node_origin_id IN " +
            " <foreach collection='childNodeOriginIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " <if test=\"originId != null\" > AND dt.origin_id = #{originId} </if>" +
            " <if test=\"ip != null\" > AND dt.ip = #{ip} </if>" +
            " <if test=\"onlineState != null\" > AND dt.online_state = #{onlineState} </if>" +
            " <if test=\"signState != null\" > AND dt.sign_state = #{signState} </if>" +
            " </script> ")
    List<GetChannelByPageRsp> selectByNodeOriginIdsAndPage(Set<String> childNodeOriginIds, String originId, String ip, Integer onlineState, Integer signState);

    @Select(" <script> " +
            " SELECT ch.id AS channelId, ch.device_id, dt.name AS channelName, ch.node_origin_id, ch.sign_state, ch.online_state, ch.create_time, " +
            " dt.origin_id, dt.ip, dt.port, dt.manufacturer, dt.model, dt.firmware, dt.ptz_type, dt.username, dt.password  FROM " + CHANNEL_TABLE_NAME + " ch " +
            " LEFT JOIN " + DetailMapper.DETAIL_TABLE_NAME + " dt ON ch.id = dt.dc_id AND type = 2 " +
            " WHERE ch.node_origin_id = #{nodeOriginId} " +
            " <if test=\"originId != null\" > AND dt.origin_id = #{originId} </if>" +
            " <if test=\"ip != null\" > AND dt.ip = #{ip} </if>" +
            " <if test=\"onlineState != null\" > AND dt.online_state = #{onlineState} </if>" +
            " <if test=\"signState != null\" > AND dt.sign_state = #{signState} </if>" +
            " </script> ")
    List<GetChannelByPageRsp> selectPlatformChannelByPage(String nodeOriginId, String originId, String ip, Integer onlineState, Integer signState);

    @Select(" <script> " +
            " SELECT ch.id AS channelId, ch.device_id, dt.name AS channelName, ch.node_origin_id, ch.sign_state, ch.online_state, ch.create_time, " +
            " dt.origin_id, dt.ip, dt.port, dt.manufacturer, dt.model, dt.firmware, dt.ptz_type, dt.username, dt.password  FROM " + CHANNEL_TABLE_NAME + " ch " +
            " LEFT JOIN " + DetailMapper.DETAIL_TABLE_NAME + " dt ON ch.id = dt.dc_id AND type = 2 " +
            " WHERE ch.sign_state != 0 " +
            " AND ch.device_id = #{deviceId} " +
            " </script> ")
    List<GetChannelByPageRsp> selectDetailByDeviceId(Long deviceId);

    @Select(" <script> " +
            " SELECT ch.id AS channelId, ch.device_id, dt.name AS channelName, ch.node_origin_id, ch.sign_state, ch.online_state, ch.create_time, " +
            " dt.origin_id, dt.ip, dt.port, dt.manufacturer, dt.model, dt.firmware, dt.ptz_type, dt.username, dt.password  FROM " + CHANNEL_TABLE_NAME + " ch " +
            " LEFT JOIN " + DetailMapper.DETAIL_TABLE_NAME + " dt ON ch.id = dt.dc_id AND type = 2 " +
            " WHERE ch.sign_state != 0 " +
            " AND ch.id IN " +
            " <foreach collection='channelIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<GetChannelByPageRsp> selectByChannelIds(Set<Long> channelIds);
}
