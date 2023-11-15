package com.runjian.device.dao;

import com.runjian.device.entity.DetailInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 设备或者通道的详细信息数据库操作类
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Mapper
@Repository
public interface DetailMapper {

    String DETAIL_TABLE_NAME = "rundo_detail";

    @Insert(" INSERT INTO " + DETAIL_TABLE_NAME +
            " (dc_id, origin_id, type, ip, port, name, manufacturer, model, firmware, ptz_type, username, password, update_time, create_time) " +
            " VALUES " +
            " (#{dcId}, #{originId}, #{type}, #{ip}, #{port}, #{name}, #{manufacturer}, #{model}, #{firmware}, #{ptzType}, #{username}, #{password}, #{updateTime}, #{createTime})")
    void save(DetailInfo detailInfo);

    @Select(" SELECT * FROM " + DETAIL_TABLE_NAME +
            " WHERE dc_id = #{dcId} AND type = #{type} for update")
    Optional<DetailInfo> selectByDcIdAndType(Long dcId, Integer type);

    @Update(" <script> " +
            " UPDATE "  + DETAIL_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test='ip != null'>, ip = #{ip}</if> " +
            " <if test='port != null'>, port = #{port}</if> " +
            " <if test='name != null'>, name = #{name}</if> " +
            " <if test='manufacturer != null'>, manufacturer = #{manufacturer}</if> " +
            " <if test='model != null'>, model = #{model}</if> " +
            " <if test='firmware != null'>, firmware = #{firmware}</if> " +
            " <if test='ptzType != null'>, ptzType = #{ptzType}</if> " +
            " <if test='username != null'>, username = #{username}</if> " +
            " <if test='password != null'>, password = #{password}</if> " +
            " WHERE id = #{id} " +
            " </script>")
    void update(DetailInfo detailInfo);

    @Delete(" DELETE FROM " + DETAIL_TABLE_NAME +
            " WHERE dc_id = #{dcId} AND type = #{type} ")
    void deleteByDcIdAndType(Long dcId, Integer type);

    @Insert({" <script> " +
            " INSERT INTO " + DETAIL_TABLE_NAME + "(dc_id, origin_id, type, ip, port, name, manufacturer, model, firmware, ptz_type, username, password, update_time, create_time) VALUES " +
            " <foreach collection='detailSaveList' item='item' separator=','>(#{item.dcId}, #{item.originId}, #{item.type}, #{item.ip}, #{item.port}, #{item.name}, #{item.manufacturer}, #{item.model}, #{item.firmware}, #{item.ptzType}, #{item.username}, #{item.password}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    void batchSave(List<DetailInfo> detailSaveList);

    @Update(" <script> " +
            " <foreach collection='detailUpdateList' item='item' separator=';'> " +
            " UPDATE " + DETAIL_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , ip = #{item.ip} " +
            " , port = #{item.port} " +
            " , name = #{item.name} " +
            " , manufacturer = #{item.manufacturer} " +
            " , model = #{item.model} " +
            " , firmware = #{item.firmware} " +
            " , ptz_type = #{item.ptzType} " +
            " , username = #{item.username} " +
            " , password = #{item.password} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script>")
    void batchUpdate(List<DetailInfo> detailUpdateList);

    @Delete(" <script> " +
            " DELETE FROM " + DETAIL_TABLE_NAME +
            " WHERE id in " +
            " <foreach collection='channelInfoIdList' item='item' open='(' separator=',' close=')' >#{item}</foreach> " +
            " AND type = #{type} " +
            " </script>")
    void deleteByDcIdsAndType(List<Long> channelInfoIdList, Integer type);

    @Select(" <script> " +
            " SELECT * FROM " + DETAIL_TABLE_NAME +
            " WHERE dc_id IN " +
            " <foreach collection='dcIds' item='item' open='(' separator=',' close=')' >#{item}</foreach> " +
            " AND type = #{detailType} " +
            " </script>")
    List<DetailInfo> selectByDcIdsAndType(Set<Long> dcIds, Integer detailType);
}
