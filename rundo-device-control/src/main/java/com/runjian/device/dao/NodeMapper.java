package com.runjian.device.dao;

import com.runjian.device.entity.NodeInfo;
import com.runjian.device.vo.response.GetNodeRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/12/14 15:51
 */
@Mapper
@Repository
public interface NodeMapper {

    String NODE_TABLE_NAME = "rundo_node";

    @Select(" SELECT * FROM " + NODE_TABLE_NAME +
            " WHERE device_id = #{deviceId} ")
    List<NodeInfo> selectByDeviceId(Long deviceId);

    @Delete(" <script> " +
            " DELETE FROM " + NODE_TABLE_NAME +
            " WHERE id IN <foreach collection='idList'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " </script>")
    void batchDelete(List<Long> idList);

    @Update(" <script> " +
            " <foreach collection='updateNodeInfoList' item='item' separator=';'> " +
            " UPDATE " + NODE_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , parent_id = #{item.parentId} " +
            " , node_name = #{item.nodeName} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdate(List<NodeInfo> updateNodeInfoList);

    @Insert(" <script> " +
            " INSERT INTO " + NODE_TABLE_NAME + "(device_id, parent_id, origin_id, node_name, update_time, create_time) VALUES " +
            " <foreach collection='saveList' item='item' separator=','>(#{item.device_id}, #{item.parentId}, #{item.originId}, #{item.nodeName}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void batchSave(List<NodeInfo> saveList);

    @Select(" <script> " +
            " SELECT * FROM " + NODE_TABLE_NAME +
            " WHERE device_id = #{deviceId} " +
            " AND origin_id IN <foreach collection='originIdList'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " </script> ")
    List<NodeInfo> selectByDeviceIdAndOriginIds(Long deviceId, List<String> originIdList);

    @Delete(" <script> " +
            " DELETE FROM " + NODE_TABLE_NAME +
            " WHERE device_id = #{deviceId} " +
            " AND origin_id IN <foreach collection='originIdList'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " </script>")
    void batchDeleteByDeviceIdAndOriginIds(Long deviceId, List<String> originIdList);

    @Select(" SELECT * FROM " + NODE_TABLE_NAME +
            " WHERE device_id = #{deviceId} ")
    List<GetNodeRsp> selectAllByDeviceId(Long deviceId);
}
