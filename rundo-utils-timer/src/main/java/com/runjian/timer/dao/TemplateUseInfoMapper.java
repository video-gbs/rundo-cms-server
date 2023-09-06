package com.runjian.timer.dao;

import com.runjian.timer.entity.TemplateUseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/9/6 15:45
 */
@Mapper
@Repository
public interface TemplateUseInfoMapper {

    String TEMPLATE_USE_TABLE_NAME = "rundo_template_use";

    void update(TemplateUseInfo templateUseInfo);

    void save(TemplateUseInfo templateUseInfo);

    List<TemplateUseInfo> selectByTemplateId(Long templateId);

    Optional<TemplateUseInfo> selectByServiceNameAndServiceUseMark(String serviceName, String serviceUseMark);

    void deleteById(Long id);
}
