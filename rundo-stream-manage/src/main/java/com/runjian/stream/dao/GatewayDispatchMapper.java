package com.runjian.stream.dao;

import com.runjian.stream.entity.GatewayDispatchInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/3 10:22
 */
@Mapper
@Repository
public interface GatewayDispatchMapper {

    String GATEWAY_DISPATCH_TABLE_NAME = "rundo_gateway_dispatch";

    @Insert({" <script> " +
            " INSERT INTO " + GATEWAY_DISPATCH_TABLE_NAME + " (gateway_id, dispatch_id, update_time, create_time) values " +
            " <foreach collection='gatewayDispatchInfoList' item='item' separator=','>(#{item.gatewayId}, #{item.dispatchId}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    void saveAll(List<GatewayDispatchInfo> gatewayDispatchInfoList);

    @Select(" SELECT * FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE gateway_id = #{gatewayId} ")
    Optional<GatewayDispatchInfo> selectByGatewayId(Long gatewayId);

    @Insert(" INSERT INTO " + GATEWAY_DISPATCH_TABLE_NAME +
            " (gateway_id, dispatch_id, update_time, create_time) " +
            " VALUES " +
            " (#{gatewayId}, #{dispatchId}, #{updateTime}, #{createTime})")
    void save(GatewayDispatchInfo gatewayDispatchInfo);

    @Update(" UPDATE " + GATEWAY_DISPATCH_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , dispatch_id = #{dispatchId} " +
            " WHERE id = #{id} ")
    void update(GatewayDispatchInfo gatewayDispatchInfo);

    @Select(" <script> " +
            " SELECT * FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE gateway_id IN " +
            " <foreach collection='gatewayIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<GatewayDispatchInfo> selectByGatewayIds(Set<Long> gatewayIds);

    @Update(" <script> " +
            " <foreach collection='gatewayDispatchInfoList' item='item' separator=';'> " +
            " UPDATE " + GATEWAY_DISPATCH_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , dispatch_id = #{item.dispatchId} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void updateAll(List<GatewayDispatchInfo> gatewayDispatchInfoList);

    @Delete(" DELETE FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE gateway_id = #{gatewayId} ")
    void deleteByGatewayId(Long gatewayId);


    @Select(" SELECT gateway_id FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE dispatch_id = #{dispatchId} ")
    List<Long> selectGatewayIdByDispatchId(Long dispatchId);

    @Delete(" DELETE FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE dispatch_id = #{dispatchId} ")
    void deleteByDispatchId(Long dispatchId);

    @Delete(" <script> " +
            " DELETE FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE dispatch_id = #{dispatchId} AND gateway_id IN " +
            " <foreach collection='gatewayIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void deleteByDispatchIdAndInGatewayIds(Long dispatchId, Set<Long> gatewayIds);
}
