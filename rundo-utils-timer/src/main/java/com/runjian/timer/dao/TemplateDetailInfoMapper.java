package com.runjian.timer.dao;

import com.runjian.timer.entity.TemplateDetailInfo;
import com.runjian.timer.vo.dto.TimePeriodDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/4 17:38
 */
@Mapper
@Repository
public interface TemplateDetailInfoMapper {

    String TEMPLATE_DETAIL_TABLE_NAME = "rundo_template_detail";

    List<TemplateDetailInfo> selectByTemplateIds(List<Long> templateIds);

    void batchSave(List<TemplateDetailInfo> templateDetailInfoList);

    void deleteByTemplateId(Long templateId);
}
