package com.runjian.timer.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.timer.service.TemplateService;
import com.runjian.timer.vo.request.PostTemplateReq;
import com.runjian.timer.vo.request.PutUnUseTemplateReq;
import com.runjian.timer.vo.request.PostUseTemplateReq;
import com.runjian.timer.vo.request.PutTemplateReq;
import com.runjian.timer.vo.response.GetTemplateInfoRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/7 15:28
 */

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
public class TemplateController {

    private final ValidatorService validatorService;

    private final TemplateService templateService;

    /**
     * 分页获取时间模板
     * @param page 页数
     * @param num 一页数据量
     * @param templateName 模板名称
     * @return
     */
    @GetMapping("/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetTemplateInfoRsp>> getTemplatePage(@RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "10") int num,
                                                                        String templateName){
        return CommonResponse.success(templateService.getTemplatePage(page, num, templateName));
    }

    /**
     * 检测时间是否可用
     * @param templateId 时间模板id
     * @param time 时间
     * @return
     */
    @GetMapping("/check/time-in")
    public CommonResponse<Boolean> checkTime(@RequestParam Long templateId, @RequestParam LocalDateTime time){
        return CommonResponse.success(templateService.checkTime(templateId, time));
    }

    /**
     * 添加时间模板
     * @param req 添加时间模板请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addTemplate(@RequestBody PostTemplateReq req){
        validatorService.validateRequest(req);
        if (!CollectionUtils.isEmpty(req.getTemplateDetailInfoList())){
            validatorService.validateRequest(req.getTemplateDetailInfoList());
        }
        templateService.addTemplate(req.getTemplateName(), req.getTemplateDetailInfoList());
        return CommonResponse.success();
    }

    /**
     * 修改时间模板
     * @param req 修改时间模板请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateTemplate(@RequestBody PutTemplateReq req){
        validatorService.validateRequest(req);
        if (!CollectionUtils.isEmpty(req.getTemplateDetailInfoList())){
            validatorService.validateRequest(req.getTemplateDetailInfoList());
        }
        templateService.updateTemplate(req.getTemplateId(), req.getTemplateName(), req.getTemplateDetailInfoList());
        return CommonResponse.success();
    }

    /**
     * 删除时间模板
     * @param templateIds 时间id数组
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteTemplate(@RequestParam Set<Long> templateIds){
        templateService.deleteTemplate(templateIds);
        return CommonResponse.success();
    }

    /**
     * 使用模板
     * @param req 使用模板请求体
     * @return
     */
    @PostMapping("/use")
    public CommonResponse<?> useTemplate(@RequestBody PostUseTemplateReq req){
        validatorService.validateRequest(req);
        templateService.useTemplate(req.getTemplateId(), req.getServiceName(), req.getServiceUseMark(), req.getEnableTimer());
        return CommonResponse.success();
    }

    /**
     * 解除模板使用
     * @param req 解除使用模板请求体
     * @return
     */
    @PutMapping("/unuse")
    public CommonResponse<?> unUseTemplate(@RequestBody PutUnUseTemplateReq req){
        validatorService.validateRequest(req);
        templateService.unUseTemplate(req.getServiceName(), req.getServiceUseMark());
        return CommonResponse.success();
    }
}
