package com.runjian.timer.dao;

import com.runjian.timer.entity.TemplateDetailInfo;
import com.runjian.timer.vo.dto.TimePeriodDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/4 17:38
 */
@Mapper
@Repository
public interface TemplateDetailInfoMapper {

    String TEMPLATE_DETAIL_TABLE_NAME = "rundo_template_detail";

    @Select(" <script> " +
            " SELECT *  FROM " + TEMPLATE_DETAIL_TABLE_NAME +
            " WHERE template_id IN "  +
            " <foreach collection='templateIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<TemplateDetailInfo> selectByTemplateIds(List<Long> templateIds);

    @Insert({" <script> " +
            " INSERT INTO " + TEMPLATE_DETAIL_TABLE_NAME + "(template_id, date_type, start_time, end_time, is_next_day, enable_timer, update_time, create_time) values " +
            " <foreach collection='templateDetailInfoList' item='item' separator=','>(#{item.templateId}, #{item.dateType}, #{item.startTime}, #{item.endTime}, #{item.isNextDay}, #{item.enableTimer}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    void batchSave(List<TemplateDetailInfo> templateDetailInfoList);

    @Delete(" <script> " +
            " DELETE FROM " + TEMPLATE_DETAIL_TABLE_NAME +
            " WHERE template_id = #{templateId} " +
            " </script>")
    void deleteByTemplateId(Long templateId);

    @Delete(" <script> " +
            " DELETE FROM " + TEMPLATE_DETAIL_TABLE_NAME +
            " WHERE template_id IN " +
            " <foreach collection='templateIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script>")
    void deleteByTemplateIds(Set<Long> templateIds);

    @Select(" <script> " +
            " SELECT *  FROM " + TEMPLATE_DETAIL_TABLE_NAME +
            " WHERE template_id = #{templateId} " +
            " AND date_type = #{dateType} " +
            " AND start_time &lt;= #{time} AND #{time} &lt;= end_time"  +
            " <foreach collection='templateIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    Optional<TemplateDetailInfo> selectByTemplateIdAndDateTypeAndTimeIn(Long templateId, Integer dateType, LocalDateTime time);
}
