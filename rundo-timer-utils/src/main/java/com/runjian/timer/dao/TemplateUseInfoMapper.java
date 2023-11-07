package com.runjian.timer.dao;

import com.runjian.timer.entity.TemplateUseInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/6 15:45
 */
@Mapper
@Repository
public interface TemplateUseInfoMapper {

    String TEMPLATE_USE_TABLE_NAME = "rundo_template_use";

    @Update(" UPDATE " + TEMPLATE_USE_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , template_id = #{templateId} " +
            " , service_name = #{serviceName} " +
            " , service_use_mark = #{serviceUseMark} " +
            " , enable_timer = #{enableTimer} " +
            " , is_init_timer = #{isInitTimer} " +
            " WHERE id = #{id} ")
    void update(TemplateUseInfo templateUseInfo);

    @Insert(" INSERT INTO " + TEMPLATE_USE_TABLE_NAME + "(template_id, service_name, service_use_mark, enable_timer, is_init_timer, update_time, create_time) values " +
            " (#{templateId}, #{serviceName}, #{serviceUseMark}, #{enableTimer}, #{isInitTimer}, #{updateTime}, #{createTime}) " )
    void save(TemplateUseInfo templateUseInfo);

    @Select(" SELECT * FROM " + TEMPLATE_USE_TABLE_NAME +
            " WHERE service_name = #{serviceName} " +
            " AND service_use_mark = #{serviceUseMark} ")
    Optional<TemplateUseInfo> selectByServiceNameAndServiceUseMark(String serviceName, String serviceUseMark);

    @Delete(" DELETE FROM " + TEMPLATE_USE_TABLE_NAME +
            " WHERE id = #{id} " )
    void deleteById(Long id);

    @Select(" <script> " +
            " SELECT * FROM " + TEMPLATE_USE_TABLE_NAME +
            " WHERE template_id IN " +
            " <foreach collection='templateIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    List<TemplateUseInfo> selectByTemplateIds(Set<Long> templateIds);
}
