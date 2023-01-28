package com.runjian.device.dao;

import com.runjian.device.entity.GatewayInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

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
            " <if test='online != null'>, online_state = #{onlineState}</if> " +
            " <if test='ip != null'>, ip = #{ip}</if> " +
            " <if test='port != null'>, port = #{port}</if> " +
            " WHERE id = #{id} "+
            " </script> "})
    void update(GatewayInfo gatewayInfo);

    @Select(" SELECT * FROM " + GATEWAY_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<GatewayInfo> selectById(Long id);

    @Update({" <script> " +
            " UPDATE "  + GATEWAY_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " online_state = #{onlineState} " +
            " WHERE id IN "+
            " <foreach collection='gatewayIds'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " </script> "})
    void batchUpdateOnlineState(Set<Long> gatewayIds, Integer code);

    @Update(" UPDATE "  + GATEWAY_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " online_state = #{onlineState} " +
            " WHERE id = #{id} ")
    void updateOnlineState(Long gatewayId, Integer onlineState);
}
