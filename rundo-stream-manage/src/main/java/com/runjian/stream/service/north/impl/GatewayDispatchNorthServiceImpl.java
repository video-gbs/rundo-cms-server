package com.runjian.stream.service.north.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.stream.dao.GatewayDispatchMapper;
import com.runjian.stream.entity.GatewayDispatchInfo;
import com.runjian.stream.feign.DeviceControlApi;
import com.runjian.stream.service.common.DataBaseService;
import com.runjian.stream.service.north.GatewayDispatchNorthService;
import com.runjian.stream.vo.request.PostGetGatewayByDispatchReq;
import com.runjian.stream.vo.response.GetGatewayByIdsRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/2/3 15:13
 */
@Service
public class GatewayDispatchNorthServiceImpl implements GatewayDispatchNorthService {

    @Autowired
    private GatewayDispatchMapper gatewayDispatchMapper;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private DeviceControlApi deviceControlApi;

    @Override
    public Long getDispatchIdByGatewayId(Long gatewayId) {
        Optional<GatewayDispatchInfo> gatewayDispatchInfoOp = gatewayDispatchMapper.selectByGatewayId(gatewayId);
        return gatewayDispatchInfoOp.map(GatewayDispatchInfo::getDispatchId).orElse(null);
    }

    @Override
    public PageInfo<GetGatewayByIdsRsp> getGatewayBindingDispatchId(int page, int num, Long dispatchId, String name) {
        List<Long> gatewayIds = gatewayDispatchMapper.selectGatewayIdByDispatchId(dispatchId);
        if (gatewayIds.isEmpty()){
            return new PageInfo<>();
        }
        CommonResponse<PageInfo<GetGatewayByIdsRsp>> response = deviceControlApi.getGatewayByIds(new PostGetGatewayByDispatchReq(page, num, gatewayIds, true, name));
        if (response.isError()){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return response.getData();
    }

    @Override
    public PageInfo<GetGatewayByIdsRsp> getGatewayNotBindingDispatchId(int page, int num, Long dispatchId, String name) {
        List<Long> gatewayIds = gatewayDispatchMapper.selectGatewayIdByDispatchId(dispatchId);
        if (Objects.isNull(gatewayIds) || gatewayIds.isEmpty()){
            CommonResponse<PageInfo<GetGatewayByIdsRsp>> response = deviceControlApi.getGatewayByName(page, num, name);
            if (response.isError()){
                throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
            }
            return response.getData();
        }
        CommonResponse<PageInfo<GetGatewayByIdsRsp>> response = deviceControlApi.getGatewayByIds(new PostGetGatewayByDispatchReq(page, num, gatewayIds, false, name));
        if (response.isError()){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return response.getData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void gatewayBindingDispatch(Long gatewayId, Long dispatchId) {
        if (Objects.isNull(dispatchId)){
            gatewayDispatchMapper.deleteByGatewayId(gatewayId);
        }
        dataBaseService.getDispatchInfo(dispatchId);

        Optional<GatewayDispatchInfo> gatewayDispatchInfoOp = gatewayDispatchMapper.selectByGatewayId(gatewayId);
        GatewayDispatchInfo gatewayDispatchInfo = gatewayDispatchInfoOp.orElse(new GatewayDispatchInfo());
        LocalDateTime nowTime = LocalDateTime.now();
        gatewayDispatchInfo.setUpdateTime(nowTime);
        if (gatewayDispatchInfoOp.isEmpty()){
            gatewayDispatchInfo  = new GatewayDispatchInfo();
            gatewayDispatchInfo.setGatewayId(gatewayId);
            gatewayDispatchInfo.setDispatchId(dispatchId);
            gatewayDispatchInfo.setCreateTime(nowTime);
            gatewayDispatchMapper.save(gatewayDispatchInfo);
        }else {
            gatewayDispatchInfo.setDispatchId(dispatchId);
            gatewayDispatchInfo.setDispatchId(dispatchId);
            gatewayDispatchMapper.update(gatewayDispatchInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatchBindingGateway(Long dispatchId, Set<Long> gatewayIds) {
        dataBaseService.getDispatchInfo(dispatchId);
        // 删除已去掉的数据
        if(gatewayIds.isEmpty()){
            gatewayDispatchMapper.deleteByDispatchId(dispatchId);
            return;
        }else {
            gatewayDispatchMapper.deleteByDispatchIdAndNotInGatewayIds(dispatchId, gatewayIds);
        }

        List<GatewayDispatchInfo> gatewayDispatchInfoList = gatewayDispatchMapper.selectByGatewayIds(gatewayIds);
        LocalDateTime nowTime = LocalDateTime.now();
        // 判断数据是否已经存在
        if (gatewayDispatchInfoList.size() > 0){
            for (GatewayDispatchInfo gatewayDispatchInfo : gatewayDispatchInfoList){
                gatewayDispatchInfo.setUpdateTime(nowTime);
                gatewayDispatchInfo.setDispatchId(dispatchId);
            }
            gatewayDispatchMapper.updateAll(gatewayDispatchInfoList);
        }

        // 判断数据是否不存在
        if (gatewayIds.size() > gatewayDispatchInfoList.size()){
            Set<Long> existGatewayIds = gatewayDispatchInfoList.stream().map(GatewayDispatchInfo::getGatewayId).collect(Collectors.toSet());
            existGatewayIds.forEach(gatewayIds::remove);
            ArrayList<GatewayDispatchInfo> gatewayDispatchInfos = new ArrayList<>(existGatewayIds.size());
            for (Long gatewayId : gatewayIds){
                GatewayDispatchInfo gatewayDispatchInfo  = new GatewayDispatchInfo();
                gatewayDispatchInfo.setGatewayId(gatewayId);
                gatewayDispatchInfo.setDispatchId(dispatchId);
                gatewayDispatchInfo.setCreateTime(nowTime);
                gatewayDispatchInfo.setUpdateTime(nowTime);
                gatewayDispatchInfos.add(gatewayDispatchInfo);
            }
            gatewayDispatchMapper.saveAll(gatewayDispatchInfos);
        }
    }
}
