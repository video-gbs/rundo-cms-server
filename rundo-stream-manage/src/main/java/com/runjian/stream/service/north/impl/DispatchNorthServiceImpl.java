package com.runjian.stream.service.north.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.stream.dao.DispatchMapper;
import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.service.common.DataBaseService;
import com.runjian.stream.service.north.DispatchNorthService;
import com.runjian.stream.vo.response.GetDispatchRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/7 19:50
 */
@Service
public class DispatchNorthServiceImpl implements DispatchNorthService {

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private DataBaseService dataBaseService;

    @Override
    public PageInfo<GetDispatchRsp> getDispatchByPage(int page, int num, String name) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(dispatchMapper.selectAllByPage(name));

    }

    @Override
    public void updateExtraData(Long dispatchId, String name, String url) {
        DispatchInfo dispatchInfo = dataBaseService.getDispatchInfo(dispatchId);
        dispatchInfo.setName(name);
        dispatchInfo.setUrl(url);
        dispatchInfo.setUpdateTime(LocalDateTime.now());
        dispatchMapper.update(dispatchInfo);
    }
}