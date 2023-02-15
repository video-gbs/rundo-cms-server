package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.feign.AuthServerApi;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.mapper.DeviceChannelExpansionMapper;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.feign.request.PutChannelSignSuccessReq;
import com.runjian.device.expansion.vo.feign.response.ChannelSyncRsp;
import com.runjian.device.expansion.vo.feign.response.GetChannelByPageRsp;
import com.runjian.device.expansion.vo.feign.response.PageListResp;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import com.runjian.device.expansion.vo.request.*;
import com.runjian.device.expansion.vo.response.ChannelExpansionFindlistRsp;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class IDeviceChannelExpansionServiceImpl extends ServiceImpl<DeviceChannelExpansionMapper, DeviceChannelExpansion> implements IDeviceChannelExpansionService {
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Autowired
    DeviceChannelExpansionMapper deviceChannelExpansionMapper;

    @Autowired
    IDeviceExpansionService deviceExpansionService;

    @Autowired
    DeviceControlApi channelControlApi;

    @Autowired
    AuthServerApi authServerApi;

    @Override
    public CommonResponse<Boolean> add(FindChannelListReq findChannelListReq) {
        //进行添加
        List<DeviceChannelExpansion> channelList = new ArrayList<>();
        ArrayList<Long> ids = new ArrayList<>();
        for (DeviceChannelExpansionAddReq deviceChannelExpansionAddReq : findChannelListReq.getChannelList()){
            DeviceChannelExpansion deviceChannelExpansion = new DeviceChannelExpansion();
            deviceChannelExpansion.setId(deviceChannelExpansionAddReq.getChannelId());
            deviceChannelExpansion.setDeviceExpansionId(deviceChannelExpansionAddReq.getDeviceExpansionId());
            deviceChannelExpansion.setChannelName(deviceChannelExpansionAddReq.getChannelName());
            deviceChannelExpansion.setChannelCode(deviceChannelExpansionAddReq.getChannelCode());
            deviceChannelExpansion.setOnlineState(deviceChannelExpansionAddReq.getOnlineState());
            deviceChannelExpansion.setVideoAreaId(findChannelListReq.getVideoAreaId());
            channelList.add(deviceChannelExpansion);
            ids.add(deviceChannelExpansionAddReq.getChannelId());
        }
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try{
            this.saveBatch(channelList);
            //通知控制服务修改添加状态
            PutChannelSignSuccessReq putChannelSignSuccessReq = new PutChannelSignSuccessReq();
            putChannelSignSuccessReq.setChannelIdList(ids);
            CommonResponse<Boolean> longCommonResponse = channelControlApi.channelSignSuccess(putChannelSignSuccessReq);
            if(longCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                dataSourceTransactionManager.rollback(transactionStatus);
                //调用失败
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",findChannelListReq, longCommonResponse);
                return longCommonResponse;
            }
            dataSourceTransactionManager.commit(transactionStatus);
        }catch(Exception e){
            dataSourceTransactionManager.rollback(transactionStatus);
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--添加异常",findChannelListReq, e);
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR,e.getMessage());
        }

        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> edit(DeviceChannelExpansionReq deviceChannelExpansionReq) {
        DeviceChannelExpansion deviceChannelExpansion = new DeviceChannelExpansion();
        BeanUtil.copyProperties(deviceChannelExpansionReq,deviceChannelExpansion);
        deviceChannelExpansionMapper.updateById(deviceChannelExpansion);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> remove(Long id) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        deviceChannelExpansionMapper.deleteById(id);
        //通知控制服务修改添加状态 删除接口待定义
        List<Long> longs = new ArrayList<>();
        longs.add(id);

        CommonResponse<Boolean> booleanCommonResponse = channelControlApi.channelDelete(longs);
        if(booleanCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            dataSourceTransactionManager.rollback(transactionStatus);
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",id, booleanCommonResponse);
            return booleanCommonResponse;
        }
        dataSourceTransactionManager.commit(transactionStatus);
        return CommonResponse.success();
    }

    @Override
    public CommonResponse<Boolean> removeBatch(List<Long> idList) {
        deviceChannelExpansionMapper.deleteBatchIds(idList);
        //通知控制服务修改添加状态 删除接口待定义
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        deviceChannelExpansionMapper.deleteBatchIds(idList);
        //通知控制服务修改添加状态 删除接口待定义
        CommonResponse<Boolean> booleanCommonResponse = channelControlApi.channelDelete(idList);
        if(booleanCommonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            dataSourceTransactionManager.rollback(transactionStatus);
            //调用失败
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器删除失败",idList, booleanCommonResponse);
            return booleanCommonResponse;
        }
        dataSourceTransactionManager.commit(transactionStatus);
        return null;
    }

    @Override
    public PageResp<DeviceChannelExpansionResp> list(DeviceChannelExpansionListReq deviceChannelExpansionListReq) {
        //安防区域的节点id
        List<Long> areaIdsArr = new ArrayList<>();
        VideoAreaResp videoAreaData = new VideoAreaResp();
        List<VideoAreaResp> videoAreaRespList = new ArrayList<>();
        if(deviceChannelExpansionListReq.getIncludeEquipment()){
            //安防区域不得为空

            CommonResponse<List<VideoAreaResp>> videoAraeList = authServerApi.getVideoAraeList(deviceChannelExpansionListReq.getVideoAreaId());
            if(CollectionUtils.isEmpty(videoAraeList.getData())){
                throw new BusinessException(BusinessErrorEnums.USER_NO_AUTH);

            }
            videoAreaRespList = videoAraeList.getData();
            areaIdsArr = videoAreaRespList.stream().map(VideoAreaResp::getId).collect(Collectors.toList());
        }else {
            if(ObjectUtils.isEmpty(deviceChannelExpansionListReq.getVideoAreaId())){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR);
            }
            CommonResponse<VideoAreaResp> videoAraeInfo = authServerApi.getVideoAraeInfo(deviceChannelExpansionListReq.getVideoAreaId());
            if(ObjectUtils.isEmpty(videoAraeInfo.getData())){
                throw new BusinessException(BusinessErrorEnums.USER_NO_AUTH);

            }
            videoAreaData = videoAraeInfo.getData();
            areaIdsArr.add(videoAreaData.getId());
        }

        LambdaQueryWrapper<DeviceChannelExpansion> queryWrapper = new LambdaQueryWrapper<>();
        if(!ObjectUtils.isEmpty(deviceChannelExpansionListReq.getName())){
            queryWrapper.like(DeviceChannelExpansion::getChannelName,deviceChannelExpansionListReq.getName());
        }
        if(!ObjectUtils.isEmpty(deviceChannelExpansionListReq.getPtzType())){
            queryWrapper.eq(DeviceChannelExpansion::getPtzType,deviceChannelExpansionListReq.getPtzType());
        }
        if(!ObjectUtils.isEmpty(deviceChannelExpansionListReq.getIp())){
            queryWrapper.like(DeviceChannelExpansion::getIp,deviceChannelExpansionListReq.getIp());
        }
        if(!ObjectUtils.isEmpty(deviceChannelExpansionListReq.getOnlineState())){
            queryWrapper.eq(DeviceChannelExpansion::getOnlineState,deviceChannelExpansionListReq.getOnlineState());
        }
        queryWrapper.eq(DeviceChannelExpansion::getDeleted,0);
        queryWrapper.in(DeviceChannelExpansion::getVideoAreaId,areaIdsArr);
        queryWrapper.orderByDesc(DeviceChannelExpansion::getCreatedAt);
        Page<DeviceChannelExpansion> page = new Page<>(deviceChannelExpansionListReq.getPageNum(), deviceChannelExpansionListReq.getPageSize());
        Page<DeviceChannelExpansion> channelExpansionPage = deviceChannelExpansionMapper.selectPage(page, queryWrapper);

        List<DeviceChannelExpansion> records = channelExpansionPage.getRecords();
        List<DeviceChannelExpansionResp> channelRespList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(records)){
            //拼接所属区域
            if(deviceChannelExpansionListReq.getIncludeEquipment()){

                for (DeviceChannelExpansion channelExpansion: records){
                    DeviceChannelExpansionResp channelExpansionResp = new DeviceChannelExpansionResp();
                    BeanUtil.copyProperties(channelExpansion,channelExpansionResp);
                    for (VideoAreaResp videoAreaResp: videoAreaRespList){
                        if(videoAreaResp.getId().equals(channelExpansion.getVideoAreaId())){
                            channelExpansionResp.setAreaNames(videoAreaResp.getAreaNames());

                        }
                    }
                    channelRespList.add(channelExpansionResp);
                }

            }else {
                for (DeviceChannelExpansion channelExpansion: records){
                    DeviceChannelExpansionResp channelExpansionResp = new DeviceChannelExpansionResp();
                    BeanUtil.copyProperties(channelExpansion,channelExpansionResp);
                    channelExpansionResp.setAreaNames(videoAreaData.getAreaNames());
                    channelRespList.add(channelExpansionResp);
                }
            }

        }
        PageResp<DeviceChannelExpansionResp> listPageResp = new PageResp<>();
        listPageResp.setCurrent(channelExpansionPage.getCurrent());
        listPageResp.setSize(channelExpansionPage.getSize());
        listPageResp.setTotal(channelExpansionPage.getTotal());
        listPageResp.setRecords(channelRespList);
        return listPageResp;
    }

    @Override
    public PageResp<ChannelExpansionFindlistRsp> findList(int page, int num, String originName) {
        //获取控制服务的添加通道列表
        CommonResponse<PageListResp<GetChannelByPageRsp>> channelByPage = channelControlApi.getChannelByPage(page, num, originName);
        //封装返回目前数据库中的编码器名称
        if(channelByPage.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--待添加的通道列表获取失败",originName, channelByPage);
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        }
        PageResp<ChannelExpansionFindlistRsp> listPageResp = new PageResp<>();
        PageListResp<GetChannelByPageRsp> originPage = channelByPage.getData();
        List<GetChannelByPageRsp> records = originPage.getList();
        List<ChannelExpansionFindlistRsp> channelExpansionFindlistRsps = new ArrayList<>();

        if(!CollectionUtils.isEmpty(records)){
            List<Long> deviceExpansionIds = records.stream().map(GetChannelByPageRsp::getDeviceId).collect(Collectors.toList());
            List<DeviceExpansion> deviceExpansionList = deviceExpansionService.listByIds(deviceExpansionIds);

            for(GetChannelByPageRsp getChannelByPageRsp : records){
                for(DeviceExpansion deviceExpansion : deviceExpansionList){
                    if(getChannelByPageRsp.getDeviceId().equals(deviceExpansion.getId())){
                        ChannelExpansionFindlistRsp channelExpansionFindlistRsp = new ChannelExpansionFindlistRsp();
                        BeanUtil.copyProperties(getChannelByPageRsp,channelExpansionFindlistRsp);
                        channelExpansionFindlistRsp.setDeviceExpansionName(deviceExpansion.getName());
                        channelExpansionFindlistRsp.setChannelCode(getChannelByPageRsp.getOriginId());
                        channelExpansionFindlistRsp.setDeviceExpansionId(getChannelByPageRsp.getDeviceId());
                        channelExpansionFindlistRsps.add(channelExpansionFindlistRsp);
                    }

                }
                
            }
        }
        listPageResp.setCurrent(originPage.getCurrent());
        listPageResp.setSize(originPage.getSize());
        listPageResp.setTotal(originPage.getTotal());
        listPageResp.setRecords(channelExpansionFindlistRsps);
        return listPageResp;
    }

    @Override
    public Boolean move(MoveReq moveReq) {
        DeviceChannelExpansion deviceExpansion = new DeviceChannelExpansion();
        moveReq.getIdList().forEach(id->{
            deviceExpansion.setVideoAreaId(moveReq.getVideoAreaId());
            deviceExpansion.setId(id);
            deviceChannelExpansionMapper.updateById(deviceExpansion);
        });

        return true;
    }

    @Override
    public CommonResponse<ChannelSyncRsp> channelSync(Long deviceId) {

        return channelControlApi.channelSync(deviceId);
    }

    @Override
    public DeviceChannelExpansion findOneDeviceByVideoAreaId(Long areaId) {
        LambdaQueryWrapper<DeviceChannelExpansion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceChannelExpansion::getVideoAreaId,areaId);
        queryWrapper.eq(DeviceChannelExpansion::getDeleted,0);
        return deviceChannelExpansionMapper.selectOne(queryWrapper);
    }
}
