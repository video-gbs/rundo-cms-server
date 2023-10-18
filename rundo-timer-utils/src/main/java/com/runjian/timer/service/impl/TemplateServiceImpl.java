package com.runjian.timer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.timer.constant.DateType;
import com.runjian.timer.dao.TemplateDetailInfoMapper;
import com.runjian.timer.dao.TemplateInfoMapper;
import com.runjian.timer.dao.TemplateUseInfoMapper;
import com.runjian.timer.entity.TemplateDetailInfo;
import com.runjian.timer.entity.TemplateInfo;
import com.runjian.timer.entity.TemplateUseInfo;
import com.runjian.timer.service.TemplateService;
import com.runjian.timer.vo.dto.TimePeriodDto;
import com.runjian.timer.vo.response.GetTemplateInfoRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/9/6 15:48
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateDetailInfoMapper templateDetailInfoMapper;

    private final TemplateInfoMapper templateInfoMapper;

    private final TemplateUseInfoMapper templateUseInfoMapper;

    @Override
    public PageInfo<GetTemplateInfoRsp> getTemplatePage(int page, int num, String templateName) {
        PageHelper.startPage(page, num);
        List<GetTemplateInfoRsp> getTemplateInfoRspList = templateInfoMapper.selectByTemplateNameLike(templateName);
        if (getTemplateInfoRspList.isEmpty()){
            return new PageInfo<>();
        }
        List<Long> templateIds = getTemplateInfoRspList.stream().map(GetTemplateInfoRsp::getId).collect(Collectors.toList());
        Map<Long, List<TemplateDetailInfo>> templateDetailMap = templateDetailInfoMapper.selectByTemplateIds(templateIds).stream().collect(Collectors.groupingBy(TemplateDetailInfo::getTemplateId, Collectors.toList()));
        for (GetTemplateInfoRsp getTemplateInfoRsp : getTemplateInfoRspList){
            List<TemplateDetailInfo> templateDetailInfos = templateDetailMap.get(getTemplateInfoRsp.getId());
            if (ObjectUtils.isEmpty(templateDetailInfos)){
                continue;
            }
            List<TimePeriodDto> timePeriodDtoList = templateDetailInfos.stream().map(TimePeriodDto::fromTemplateDetailInfo).collect(Collectors.toList());
            getTemplateInfoRsp.setTimePeriodDtoList(timePeriodDtoList);
            Set<Integer> dateTypeSet = timePeriodDtoList.stream().map(TimePeriodDto::getDateType).collect(Collectors.toSet());
            getTemplateInfoRsp.setDateTypeStrList(dateTypeSet.stream().map(DateType::getMsgByCode).collect(Collectors.toList()));
        }
        return new PageInfo<>(getTemplateInfoRspList);
    }

    @Override
    public Boolean checkTime(Long templateId, LocalDateTime time) {
        return templateDetailInfoMapper.selectByTemplateIdAndDateTypeAndTimeIn(templateId, time.getDayOfWeek().getValue(), time).isPresent();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTemplate(String templateName, List<TemplateDetailInfo> templateDetailInfoList) {
        Optional<TemplateInfo> templateInfoOptional = templateInfoMapper.selectByTemplateName(templateName);
        if(templateInfoOptional.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("模板名称'%s'已存在", templateName));
        }
        LocalDateTime nowTime = LocalDateTime.now();
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setTemplateName(templateName);
        templateInfo.setCreateTime(nowTime);
        templateInfo.setUpdateTime(nowTime);
        templateInfoMapper.save(templateInfo);

        if (!CollectionUtils.isEmpty(templateDetailInfoList)){
            for (TemplateDetailInfo templateDetailInfo : templateDetailInfoList) {
                templateDetailInfo.setTemplateId(templateInfo.getId());
                templateDetailInfo.setCreateTime(nowTime);
                templateDetailInfo.setUpdateTime(nowTime);
                templateDetailInfo.setEnableTimer(CommonEnum.DISABLE.getCode());
            }
            templateDetailInfoMapper.batchSave(templateDetailInfoList);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(Long templateId, String templateName, List<TemplateDetailInfo> templateDetailInfoList) {
        Optional<TemplateInfo> templateInfoOptional = templateInfoMapper.selectById(templateId);
        if(templateInfoOptional.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("模板'%s'不存在", templateId));
        }
        TemplateInfo templateInfo = templateInfoOptional.get();
        // 校验模板名称是否已存在
        if (!Objects.equals(templateName, templateInfo.getTemplateName()) && templateInfoMapper.selectByTemplateName(templateName).isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("模板名称'%s'已存在", templateName));
        }
        LocalDateTime nowTime = LocalDateTime.now();
        templateInfo.setTemplateName(templateName);
        templateInfo.setUpdateTime(nowTime);
        templateInfoMapper.update(templateInfo);

        // 删除原有模板明细信息
        templateDetailInfoMapper.deleteByTemplateId(templateId);
        // 保存新的模板明细信息
        for (TemplateDetailInfo templateDetailInfo : templateDetailInfoList) {
            templateDetailInfo.setTemplateId(templateInfo.getId());
            templateDetailInfo.setEnableTimer(CommonEnum.DISABLE.getCode());
            templateDetailInfo.setCreateTime(nowTime);
            templateDetailInfo.setUpdateTime(nowTime);
        }
        templateDetailInfoMapper.batchSave(templateDetailInfoList);

        // todo 重置定时器
        // List<TemplateUseInfo> templateUseInfoList = templateUseInfoMapper.selectByTemplateIdAndEnableTimer(templateId, CommonEnum.ENABLE.getCode());

    }

    @Override
    public void useTemplate(Long templateId, String serviceName, String serviceUseMark, Integer enableTimer) {
        if (templateInfoMapper.selectById(templateId).isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("模板'%s'不存在", templateId));
        }
        Optional<TemplateUseInfo> templateUseInfoOptional = templateUseInfoMapper.selectByServiceNameAndServiceUseMark(serviceName, serviceUseMark);
        TemplateUseInfo templateUseInfo = templateUseInfoOptional.orElse(new TemplateUseInfo());
        LocalDateTime nowTime = LocalDateTime.now();
        if (templateUseInfoOptional.isPresent()){
            boolean isUpdate = false;
            if (!Objects.equals(templateUseInfo.getTemplateId(), templateId)){
                isUpdate = true;
                templateUseInfo.setTemplateId(templateId);
            }
            if (!Objects.equals(templateUseInfo.getEnableTimer(), enableTimer)){
                isUpdate = true;
                templateUseInfo.setEnableTimer(enableTimer);
//                if (Objects.equals(enableTimer, CommonEnum.ENABLE.getCode())){
//                    // todo 启用定时器，判断是否第一次初始化定时器
//                } else {
//                    // todo 关闭定时器
//                }
            }
            if (isUpdate){
                templateUseInfo.setUpdateTime(nowTime);
                templateUseInfoMapper.update(templateUseInfo);
            }
        }else {
            templateUseInfo.setTemplateId(templateId);
            templateUseInfo.setServiceName(serviceName);
            templateUseInfo.setServiceUseMark(serviceUseMark);
            templateUseInfo.setEnableTimer(enableTimer);
            templateUseInfo.setCreateTime(nowTime);
            templateUseInfo.setUpdateTime(nowTime);
            templateUseInfoMapper.save(templateUseInfo);
//            if (Objects.equals(enableTimer, CommonEnum.ENABLE.getCode())){
//                // todo 启用定时器，判断是否第一次初始化定时器
//            } else {
//                // todo 关闭定时器
//            }
        }

    }

    @Override
    public void unUseTemplate(String serviceName, String serviceUseMark) {
        Optional<TemplateUseInfo> templateUseInfoOptional = templateUseInfoMapper.selectByServiceNameAndServiceUseMark(serviceName, serviceUseMark);
        if (templateUseInfoOptional.isPresent()){
            TemplateUseInfo templateUseInfo = templateUseInfoOptional.get();
//            if (CommonEnum.getBoolean(templateUseInfo.getIsInitTimer())){
//                // todo 删除所有定时器
//            }
            templateUseInfoMapper.deleteById(templateUseInfo.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(Set<Long> templateIds) {
        if(CollectionUtils.isEmpty(templateIds)){
            return;
        }
        // 查询仍然存在的模板
        List<TemplateInfo> templateInfos = templateInfoMapper.selectByIds(templateIds);
        Set<Long> existTemplateIds = templateInfos.stream().map(TemplateInfo::getId).collect(Collectors.toSet());
        List<TemplateUseInfo> templateUseInfoList = templateUseInfoMapper.selectByTemplateIds(existTemplateIds);
        if (!templateUseInfoList.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "存在模板正在被使用，无法删除");
        }
        templateInfoMapper.deleteByIds(existTemplateIds);
        templateDetailInfoMapper.deleteByTemplateIds(existTemplateIds);
    }
}
