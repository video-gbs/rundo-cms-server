package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.PtzType;
import com.runjian.device.expansion.entity.ChannelPresetLists;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.mapper.ChannelPresetMapper;
import com.runjian.device.expansion.service.IChannelPresetService;
import com.runjian.device.expansion.vo.feign.request.FeignPtz3dReq;
import com.runjian.device.expansion.vo.feign.request.FeignPtzControlReq;
import com.runjian.device.expansion.vo.request.ChannelPresetControlReq;
import com.runjian.device.expansion.vo.request.ChannelPresetEditReq;
import com.runjian.device.expansion.vo.request.Ptz3dReq;
import com.runjian.device.expansion.vo.response.ChannelPresetListsResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class ChannelPresetServiceImpl extends ServiceImpl<ChannelPresetMapper, ChannelPresetLists> implements IChannelPresetService {

    @Autowired
    ChannelPresetMapper channelPresetMapper;

    @Autowired
    DeviceControlApi deviceControlApi;

    @Override
    public CommonResponse<List<ChannelPresetListsResp>> presetSelect(Long channelExpansionId) {
        //考虑部分数据修改
        LambdaQueryWrapper<ChannelPresetLists> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelPresetLists::getChannelExpansionId,channelExpansionId);
        queryWrapper.eq(ChannelPresetLists::getDeleted,0);
        List<ChannelPresetLists> channelPresetLists = channelPresetMapper.selectList(queryWrapper);
        List<ChannelPresetListsResp> channelPresetListsResps = new ArrayList<>();
        //获取数据库数据
        if(ObjectUtils.isEmpty(channelPresetLists)){
            //获取预置位的信息  并且插入到数据库中
            CommonResponse<List<ChannelPresetListsResp>> ptzPresetCompose = deviceControlApi.getPtzPreset(channelExpansionId);
            if(ptzPresetCompose.getCode() == BusinessErrorEnums.SUCCESS.getErrCode()){
                List<ChannelPresetListsResp> feignPtzPresetList = ptzPresetCompose.getData();
                //插入数据库 并且返回数据
                List<ChannelPresetLists> channelPresetListsDao = BeanUtil.copyToList(feignPtzPresetList, ChannelPresetLists.class);
                channelPresetListsDao.forEach(channelPresetListDto -> {
                    channelPresetListDto.setChannelExpansionId(channelExpansionId);
                });
                this.saveBatch(channelPresetListsDao);
                //返回数据
                channelPresetListsResps = feignPtzPresetList;

            }

        }else {
            channelPresetListsResps = BeanUtil.copyToList(channelPresetLists, ChannelPresetListsResp.class);
        }

        return CommonResponse.success(channelPresetListsResps);
    }

    @Override
    public CommonResponse<Boolean> presetEdit(ChannelPresetEditReq channelPresetEditReq) {
        //预置位编辑和设置
        //哦按段
        LambdaQueryWrapper<ChannelPresetLists> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelPresetLists::getChannelExpansionId,channelPresetEditReq.getChannelExpansionId());
        queryWrapper.eq(ChannelPresetLists::getPresetId,channelPresetEditReq.getPresetId());
        queryWrapper.eq(ChannelPresetLists::getDeleted,0);
        Long aLong = channelPresetMapper.selectCount(queryWrapper);
        ChannelPresetLists channelPresetLists = new ChannelPresetLists();
        if(aLong <= 0 ){
            //数据不存在 重新调用和插入数据库
            FeignPtzControlReq feignPtzControlReq = new FeignPtzControlReq();
            feignPtzControlReq.setChannelId(channelPresetEditReq.getChannelExpansionId());
            feignPtzControlReq.setCmdCode(PtzType.PRESET_SET.getCode());
            feignPtzControlReq.setCmdValue(channelPresetEditReq.getPresetId());
            //调用feign 进行预置位设置
            CommonResponse<?> commonResponse = deviceControlApi.ptzControl(feignPtzControlReq);
            if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR,commonResponse.getMsg());
            }


            channelPresetLists.setChannelExpansionId(channelPresetEditReq.getChannelExpansionId());
            channelPresetLists.setPresetId(channelPresetEditReq.getPresetId());
            channelPresetLists.setPresetName(channelPresetEditReq.getPresetName());
            channelPresetMapper.insert(channelPresetLists);

        }else {
            //数据存在进行修改数据库，修改对应通道的中文
            LambdaQueryWrapper<ChannelPresetLists> editQueryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ChannelPresetLists::getChannelExpansionId,channelPresetEditReq.getChannelExpansionId());
            queryWrapper.eq(ChannelPresetLists::getPresetId,channelPresetEditReq.getPresetId());
            queryWrapper.eq(ChannelPresetLists::getDeleted,0);
            channelPresetLists.setPresetName(channelPresetLists.getPresetName());
            channelPresetMapper.update(channelPresetLists,editQueryWrapper);
        }



        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> presetDelete(ChannelPresetControlReq channelPresetControlReq) {
        LambdaQueryWrapper<ChannelPresetLists> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelPresetLists::getChannelExpansionId,channelPresetControlReq.getChannelExpansionId());
        Long one = channelPresetMapper.selectCount(queryWrapper);
        if(one <=0 ){
            //预置位不存在
            throw new BusinessException(BusinessErrorEnums.RESULT_DATA_NONE);
        }
        FeignPtzControlReq feignPtzControlReq = new FeignPtzControlReq();
        feignPtzControlReq.setChannelId(channelPresetControlReq.getChannelExpansionId());
        feignPtzControlReq.setCmdCode(PtzType.PRESET_DEL.getCode());
        feignPtzControlReq.setCmdValue(channelPresetControlReq.getPresetId());
        //调用feign 进行预置位设置
        CommonResponse<?> commonResponse = deviceControlApi.ptzControl(feignPtzControlReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR,commonResponse.getMsg());
        }
        ChannelPresetLists channelPresetLists = new ChannelPresetLists();
        LambdaQueryWrapper<ChannelPresetLists> editQueryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelPresetLists::getChannelExpansionId,channelPresetControlReq.getChannelExpansionId());
        queryWrapper.eq(ChannelPresetLists::getPresetId,channelPresetControlReq.getPresetId());
        channelPresetLists.setDeleted(1);
        channelPresetMapper.update(channelPresetLists,editQueryWrapper);

        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> presetInvoke(ChannelPresetControlReq channelPresetControlReq) {
        LambdaQueryWrapper<ChannelPresetLists> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelPresetLists::getChannelExpansionId,channelPresetControlReq.getChannelExpansionId());
        Long one = channelPresetMapper.selectCount(queryWrapper);
        if(one <=0 ){
            //预置位不存在
            throw new BusinessException(BusinessErrorEnums.RESULT_DATA_NONE);
        }
        FeignPtzControlReq feignPtzControlReq = new FeignPtzControlReq();
        feignPtzControlReq.setChannelId(channelPresetControlReq.getChannelExpansionId());
        feignPtzControlReq.setCmdCode(PtzType.PRESET_INVOKE.getCode());
        feignPtzControlReq.setCmdValue(channelPresetControlReq.getPresetId());
        //调用feign 进行预置位设置
        CommonResponse<?> commonResponse = deviceControlApi.ptzControl(feignPtzControlReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR,commonResponse.getMsg());
        }
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<?> dragZoom(Ptz3dReq request) {
        FeignPtz3dReq feignPtz3dReq = new FeignPtz3dReq();
        BeanUtil.copyProperties(request,feignPtz3dReq);
        feignPtz3dReq.setChannelId(request.getChannelExpansionId());
        return deviceControlApi.ptz3d(feignPtz3dReq);
    }
}
