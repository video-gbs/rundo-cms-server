package com.runjian.cascade.service.impl;

import com.github.pagehelper.PageInfo;
import com.runjian.cascade.constant.PlatformSignState;
import com.runjian.cascade.dao.PlatformMapper;
import com.runjian.cascade.entity.PlatformInfo;
import com.runjian.cascade.service.PlatformService;
import com.runjian.cascade.vo.response.GetPlatformPageRsp;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.SignState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/12/26 16:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService {

    private final PlatformMapper platformMapper;

    @Override
    public PageInfo<GetPlatformPageRsp> getPlatformPage(int page, int num, String name, String ip) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPlatform(String name, String gbCode, String ip, Integer port, String username, String password) {
        Optional<PlatformInfo> platformInfoOp = platformMapper.selectByIpAndPort(ip, port);
        PlatformInfo platformInfo = platformInfoOp.orElse(new PlatformInfo());
        if (platformInfoOp.isEmpty()){
            platformInfo.setName(name);
            platformInfo.setGbCode(gbCode);
            platformInfo.setIp(ip);
            platformInfo.setPort(port);
            platformInfo.setSignState(PlatformSignState.LOGOUT.getCode());
            platformInfo.setUsername(username);
            platformInfo.setPassword(password);
            platformMapper.save(platformInfo);
        }else {
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("平台 %s:%s 已存在", ip, port));
        }
    }

    @Override
    public void updatePlatform(Long platformId, String name, String gbCode, String ip, Integer port, String username, String password) {
        Optional<PlatformInfo> platformInfoOp = platformMapper.selectById(platformId);
        PlatformInfo platformInfo = platformInfoOp.orElse(new PlatformInfo());
        if (platformInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("平台 platformId 不存在", ip, port));
        }
        if (Objects.equals(platformInfo.getIp(), ip) && Objects.equals(platformInfo.getPort(), port)){
            platformInfo.setName(name);
            platformInfo.setGbCode(gbCode);
            platformInfo.setUsername(username);
            platformInfo.setPassword(password);
        }

        platformInfo.setIp(ip);
        platformInfo.setPort(port);

        platformMapper.update(platformInfo);
    }



    @Override
    public void batchDeletePlatform(Set<Long> platformIds) {

    }
}
