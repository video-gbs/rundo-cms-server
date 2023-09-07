package com.runjian.timer.dao;

import com.runjian.timer.entity.TemplateInfo;
import com.runjian.timer.vo.response.GetTemplateInfoRsp;
import org.apache.ibatis.annotations.Mapper;
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

    List<GetTemplateInfoRsp> selectByTemplateNameLike(String templateName);

    Optional<TemplateInfo> selectByTemplateName(String templateName);


    void save(TemplateInfo templateInfo);

    Optional<TemplateInfo> selectById(Long templateId);

    void deleteById(Long templateId);

    List<TemplateInfo> selectByIds(Set<Long> templateIds);

    void deleteByIds(Set<Long> templateIds);
}
