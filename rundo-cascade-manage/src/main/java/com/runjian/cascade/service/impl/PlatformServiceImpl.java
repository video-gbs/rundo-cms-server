package com.runjian.cascade.service.impl;

import com.github.pagehelper.PageInfo;
import com.runjian.cascade.constant.PlatformSignState;
import com.runjian.cascade.dao.PlatformMapper;
import com.runjian.cascade.entity.PlatformInfo;
import com.runjian.cascade.gb28181Module.gb28181.bean.OtherPlatform;
import com.runjian.cascade.gb28181Module.service.IPlatformCommandService;
import com.runjian.cascade.service.PlatformService;
import com.runjian.cascade.utils.DataConvertUtils;
import com.runjian.cascade.utils.ThreadPoolUtils;
import com.runjian.cascade.vo.response.GetPlatformPageRsp;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.SignState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/12/26 16:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService {

    private final PlatformMapper platformMapper;

    private final IPlatformCommandService platformCommandService;

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
            LocalDateTime nowTime = LocalDateTime.now();
            platformInfo.setName(name);
            platformInfo.setGbCode(gbCode);
            platformInfo.setIp(ip);
            platformInfo.setPort(port);
            platformInfo.setSignState(PlatformSignState.SIGN_OUT.getCode());
            platformInfo.setUsername(username);
            platformInfo.setPassword(password);
            platformInfo.setUpdateTime(nowTime);
            platformInfo.setCreateTime(nowTime);
            platformMapper.save(platformInfo);
        }else {
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("平台 %s:%s 已存在", ip, port));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlatform(Long platformId, String name, String gbCode, String ip, Integer port, String username, String password) {
        Optional<PlatformInfo> platformInfoOp = platformMapper.selectById(platformId);

        if (platformInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("平台 platformId 不存在", ip, port));
        }
        PlatformInfo platformInfo = platformInfoOp.get();
        LocalDateTime nowTime = LocalDateTime.now();
        boolean isUpdate = false;
        if (!Objects.equals(platformInfo.getIp(), ip) || !Objects.equals(platformInfo.getPort(), port)){
            if (platformMapper.selectByIpAndPort(ip, port).isPresent()){
                throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("平台 %s:%s 已存在", ip, port));
            }
            isUpdate = true;
        }
        if (isUpdate || !Objects.equals(platformInfo.getUsername(), username) ||
                !Objects.equals(platformInfo.getPassword(), password) || !Objects.equals(platformInfo.getGbCode(), gbCode)){
            platformInfo.setIp(ip);
            platformInfo.setPort(port);
            platformInfo.setGbCode(gbCode);
            platformInfo.setUsername(username);
            platformInfo.setPassword(password);
            platformInfo.setUpdateTime(nowTime);
            ThreadPoolUtils.workExecutor.execute(() -> {
                OtherPlatform otherPlatform = DataConvertUtils.toOtherPlatform(platformInfo);
                platformCommandService.unRegister(otherPlatform);
                if (platformCommandService.register(otherPlatform)){
                    platformInfo.setOnlineState(CommonEnum.ENABLE.getCode());
                    platformInfo.setSignState(PlatformSignState.SIGN_IN.getCode());
                }else {
                    platformInfo.setOnlineState(CommonEnum.DISABLE.getCode());
                    platformInfo.setSignState(PlatformSignState.SIGN_OUT.getCode());
                }
                // todo 流全部关闭
                platformMapper.update(platformInfo);
            });

        }else {
            platformInfo.setName(name);
            platformMapper.update(platformInfo);
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeletePlatform(Set<Long> platformIds) {
        if (platformIds.isEmpty()){
            return;
        }
        List<PlatformInfo> platformInfoList = platformMapper.selectAllByIds(platformIds);
        if (platformInfoList.isEmpty()){
            return;
        }
        List<Long> ids = platformInfoList.stream().map(platformInfo -> {
            platformCommandService.unRegister(DataConvertUtils.toOtherPlatform(platformInfo));
            return platformInfo.getId();
        }).collect(Collectors.toList());
        // todo 流全部关闭
        platformMapper.batchDelete(ids);
    }
}
