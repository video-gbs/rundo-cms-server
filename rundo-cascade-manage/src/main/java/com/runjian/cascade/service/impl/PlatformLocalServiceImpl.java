package com.runjian.cascade.service.impl;

import com.runjian.cascade.service.PlatformLocalService;
import com.runjian.cascade.vo.response.GetPlatformInfoRsp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Miracle
 * @date 2023/12/11 15:00
 */
@Service
@RequiredArgsConstructor
public class PlatformLocalServiceImpl implements PlatformLocalService {

    private final GetPlatformInfoRsp getPlatformInfoRsp;

    @Override
    public GetPlatformInfoRsp getPlatformInfo() {
        return this.getPlatformInfoRsp;
    }
}
