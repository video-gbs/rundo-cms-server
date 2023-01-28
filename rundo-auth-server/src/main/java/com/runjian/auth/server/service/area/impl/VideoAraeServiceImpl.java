package com.runjian.auth.server.service.area.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.entity.video.VideoArea;
import com.runjian.auth.server.mapper.video.VideoAraeMapper;
import com.runjian.auth.server.model.vo.video.AreaNode;
import com.runjian.auth.server.service.area.VideoAraeService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 安保区域 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class VideoAraeServiceImpl extends ServiceImpl<VideoAraeMapper, VideoArea> implements VideoAraeService {

    @Autowired
    private VideoAraeMapper videoAraeMapper;

    @Override
    public List<AreaNode> getTreeList() {
        QueryWrapper<VideoArea> queryWrapper = new QueryWrapper<>();
        List<VideoArea> videoList = videoAraeMapper.selectList(queryWrapper);
        List<AreaNode> areaNodeList = videoList.stream().map(
                item -> {
                    AreaNode bean = new AreaNode();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buiidTree(areaNodeList, 1L);
    }

    @Override
    public List<AreaNode> getTreeList(Long id, String areaName) {
        if (id != null) {
            List<VideoArea> videoList = videoAraeMapper.selectTree(id, areaName);
            List<AreaNode> areaNodeList = videoList.stream().map(
                    item -> {
                        AreaNode bean = new AreaNode();
                        BeanUtils.copyProperties(item, bean);
                        return bean;
                    }
            ).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(areaName)) {
                return areaNodeList;
            } else {
                return DataTreeUtil.buiidTree(areaNodeList, id);
            }
        } else {
            throw new RuntimeException("查询安保区域ID不能为空");
        }
    }

    @Override
    public void saveVideoArae(VideoArea dto) {
        videoAraeMapper.insert(dto);
    }
}
