package com.runjian.timer.service;

import com.github.pagehelper.PageInfo;
import com.runjian.timer.entity.TemplateDetailInfo;
import com.runjian.timer.vo.response.GetTemplateInfoRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/4 17:37
 */
public interface TemplateService {

    /**
     * 查询模板
     * @param page 页数
     * @param num 一页数据量
     * @param templateName 模板名称
     * @return
     */
    PageInfo<GetTemplateInfoRsp> getTemplateList(int page, int num, String templateName);

    /**
     * 添加模板
     * @param templateName 模板名称
     */
    void addTemplate(String templateName, List<TemplateDetailInfo> templateDetailInfoList);

    /**
     * 修改模板
     * @param templateId 模板id
     * @param templateName 模板名称
     * @param templateDetailInfoList 时间段
     */
    void updateTemplate(Long templateId, String templateName, List<TemplateDetailInfo> templateDetailInfoList);

    /**
     * 使用模板
     * @param templateId 模板id
     * @param serviceName 服务名称
     * @param serviceUseMark 服务使用标志
     * @param enableTimer 是否启用定时器
     */
    void useTemplate(Long templateId, String serviceName, String serviceUseMark, Integer enableTimer);

    /**
     * 不使用模板
     * @param serviceName 服务名称
     * @param serviceUseMark 服务使用标志
     */
    void unUseTemplate(String serviceName, String serviceUseMark);

    /**
     * 删除模板
     * @param templateId 模板id
     */
    void deleteTemplate(Long templateId);
}
