package com.runjian.device.dao;

import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.vo.response.GetGatewayByIdsRsp;
import com.runjian.device.vo.response.GetGatewayNameRsp;
import com.runjian.device.vo.response.GetGatewayPageRsp;
import com.runjian.device.vo.response.GetGatewayRsp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 网关数据库操作类
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Mapper
@Repository
public interface GatewayMapper {

    String GATEWAY_TABLE_NAME = "rundo_gateway";

    @Insert(" INSERT INTO " + GATEWAY_TABLE_NAME +
            " (id, serial_num, name, sign_type, gateway_type, protocol, ip, port, update_time, create_time) " +
            " VALUES " +
            " (#{id}, #{serialNum}, #{name}, #{signType}, #{gatewayType}, #{protocol}, #{ip}, #{port}, #{updateTime}, #{createTime})")
    void save(GatewayInfo gatewayInfo);

    @Update({" <script> " +
            " UPDATE "  + GATEWAY_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test='name != null'>, name = #{name}</if> " +
            " <if test='signType != null'>, sign_type = #{signType}</if> " +
            " <if test='onlineState != null'>, online_state = #{onlineState}</if> " +
            " <if test='ip != null'>, ip = #{ip}</if> " +
            " <if test='port != null'>, port = #{port}</if> " +
            " WHERE id = #{id} "+
            " </script> "})
    void update(GatewayInfo gatewayInfo);

    @Select(" SELECT * FROM " + GATEWAY_TABLE_NAME +
            " WHERE id = #{gatewayId} ")
    Optional<GatewayInfo> selectById(Long gatewayId);

    @Update({" <script> " +
            " UPDATE "  + GATEWAY_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " online_state = #{onlineState} " +
            " WHERE id IN "+
            " <foreach collection='gatewayIds'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " </script> "})
    void batchUpdateOnlineState(Set<Long> gatewayIds, Integer onlineState, LocalDateTime updateTime);

    @Update(" UPDATE "  + GATEWAY_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " online_state = #{onlineState} " +
            " WHERE id = #{gatewayId} ")
    void updateOnlineState(Long gatewayId, Integer onlineState, LocalDateTime updateTime);

    @Select(" <script> " +
            " SELECT id, name, protocol FROM " + GATEWAY_TABLE_NAME+
            " WHERE 1=1 " +
            " <if test=\"gatewayId != null\" > AND id = #{gatewayId} </if> " +
            " </script> ")
    List<GetGatewayNameRsp> selectAllNameAndId(Long gatewayId);

    @Select(" <script> " +
            " SELECT * FROM " + GATEWAY_TABLE_NAME +
            " WHERE 1=1 " +
            "<if test=\"name != null\" > AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            " </script> ")
    List<GetGatewayPageRsp> selectByPage(String name);

    @Select(" <script> " +
            " SELECT * FROM " + GATEWAY_TABLE_NAME +
            " WHERE "+
            " <if test=\"isIn == true\" > id IN </if>" +
            " <if test=\"isIn == false\" > id NOT IN </if> " +
            " <foreach collection='gatewayIds'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " <if test=\"name != null\" > AND name LIKE CONCAT('%', #{name}, '%') </if>" +
            " </script> ")
    List<GetGatewayByIdsRsp> selectByIds(List<Long> gatewayIds, Boolean isIn, String name);

    @Select(" SELECT id FROM " + GATEWAY_TABLE_NAME +
            " WHERE online_state = #{onlineState} ")
    Set<Long> selectIdByOnlineState(Integer onlineState);

    @Select(" SELECT * FROM " + GATEWAY_TABLE_NAME + " gt " +
            " LEFT JOIN " + DeviceMapper.DEVICE_TABLE_NAME + " dt ON dt.gateway_id = gt.id " +
            " LEFT JOIN " + ChannelMapper.CHANNEL_TABLE_NAME + " ct ON ct.device_id = dt.id " +
            " WHERE ct.id = #{channelId} ")
    GetGatewayRsp selectByChannelId(Long channelId);
}
