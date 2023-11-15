package com.runjian.timer.dao;

import com.runjian.timer.entity.TemplateInfo;
import com.runjian.timer.vo.response.GetTemplateInfoRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/4 17:38
 */
@Mapper
@Repository
public interface TemplateInfoMapper {

    String TEMPLATE_TABLE_NAME = "rundo_template";

    @Select(" <script> " +
            " SELECT * FROM " + TEMPLATE_TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"template_name != null\" >  AND template_name LIKE CONCAT('%', #{templateName}, '%') </if> " +
            " ORDER BY update_time desc " +
            " </script> ")
    List<GetTemplateInfoRsp> selectByTemplateNameLike(String templateName);

    @Select(" SELECT * FROM " + TEMPLATE_TABLE_NAME +
            " WHERE template_name = #{templateName} ")
    Optional<TemplateInfo> selectByTemplateName(String templateName);

    @Insert(" INSERT INTO " + TEMPLATE_TABLE_NAME + "(template_name, update_time, create_time) values " +
            " (#{templateName}, #{updateTime}, #{createTime}) " )
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(TemplateInfo templateInfo);

    @Select(" SELECT * FROM " + TEMPLATE_TABLE_NAME +
            " WHERE id = #{templateId} ")
    Optional<TemplateInfo> selectById(Long templateId);

    @Delete(" DELETE FROM " + TEMPLATE_TABLE_NAME +
            " WHERE id = #{templateId} " )
    void deleteById(Long templateId);

    @Select(" <script> " +
            " SELECT *  FROM " + TEMPLATE_TABLE_NAME +
            " WHERE id IN "  +
            " <foreach collection='templateIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<TemplateInfo> selectByIds(Set<Long> templateIds);

    @Delete(" <script> " +
            " DELETE FROM " + TEMPLATE_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='templateIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    void deleteByIds(Set<Long> templateIds);

    @Update(" <script> " +
            " UPDATE " + TEMPLATE_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , template_name = #{templateName} " +
            " WHERE id = #{id} " +
            " </script> ")
    void update(TemplateInfo templateInfo);
}
