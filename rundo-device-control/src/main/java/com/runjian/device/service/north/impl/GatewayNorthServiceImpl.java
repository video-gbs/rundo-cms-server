package com.runjian.device.service.north.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.service.north.GatewayNorthService;
import com.runjian.device.vo.response.GetGatewayByIdsRsp;
import com.runjian.device.vo.response.GetGatewayNameRsp;
import com.runjian.device.vo.response.GetGatewayPageRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/6 17:21
 */
@Service
public class GatewayNorthServiceImpl implements GatewayNorthService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Override
    public List<GetGatewayNameRsp> getGatewayNameList() {
        return gatewayMapper.selectAllNameAndId();
    }

    /**
     * 获取网关信息分页
     * @param page 页码
     * @param num 每页数量
     * @param name 网关名称
     * @return
     */
    @Override
    public PageInfo<GetGatewayPageRsp> getGatewayByPage(int page, int num, String name) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(gatewayMapper.selectByPage(name));
    }

    /**
     * 修改网关名称
     * @param gatewayId 网关id
     * @param name 名称
     */
    @Override
    public void updateGateway(Long gatewayId, String name) {
        Optional<GatewayInfo> gatewayInfoOptional = gatewayMapper.selectById(gatewayId);
        if (gatewayInfoOptional.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("网关%s不存在", gatewayId));
        }
        GatewayInfo gatewayInfo = gatewayInfoOptional.get();
        gatewayInfo.setName(name);
        gatewayMapper.update(gatewayInfo);
    }

    /**
     * 根据网关id数组和名称分页查找网关信息
     * @param page 页码
     * @param num 每页数据量
     * @param gatewayIds 网关id数组
     * @param isIn 包含还是不包含
     * @param name 名称
     * @return
     */
    @Override
    public PageInfo<GetGatewayByIdsRsp> getGatewayByIds(int page, int num, List<Long> gatewayIds, Boolean isIn, String name) {
        if (Objects.isNull(gatewayIds) || gatewayIds.size() == 0 || Objects.isNull(isIn)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR);
        }
        PageHelper.startPage(page, num);
        return new PageInfo<>(gatewayMapper.selectByIds(gatewayIds, isIn, name));
    }

}
