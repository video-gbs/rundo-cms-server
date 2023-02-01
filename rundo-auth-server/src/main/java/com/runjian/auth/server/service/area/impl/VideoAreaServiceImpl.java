package com.runjian.auth.server.service.area.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.video.AddVideoAreaDTO;
import com.runjian.auth.server.domain.dto.video.UpdateVideoAreaDTO;
import com.runjian.auth.server.domain.entity.video.VideoArea;
import com.runjian.auth.server.domain.vo.tree.VideoAreaTree;
import com.runjian.auth.server.domain.vo.video.MoveVideoAreaDTO;
import com.runjian.auth.server.domain.vo.video.VideoAreaVO;
import com.runjian.auth.server.mapper.video.VideoAraeMapper;
import com.runjian.auth.server.service.area.VideoAreaSaervice;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class VideoAreaServiceImpl extends ServiceImpl<VideoAraeMapper, VideoArea> implements VideoAreaSaervice {

    @Autowired
    private VideoAraeMapper videoAraeMapper;

    @Override
    public void saveVideoArae(AddVideoAreaDTO dto) {
        VideoArea area = new VideoArea();
        area.setAreaName(dto.getAreaName());
        area.setAreaPid(dto.getAreaPid());
        // 查取上级节点的Pids
        VideoArea prentInfo = videoAraeMapper.selectById(dto.getAreaPid());
        String pids = prentInfo.getAreaPids() + ",[" + dto.getAreaPid() + "]";
        area.setAreaPids(pids);
        area.setDescription(dto.getDescription());
        int pLevel = Integer.parseInt(prentInfo.getLevel());
        pLevel = pLevel + 1;
        area.setLevel(Integer.toString(pLevel));
        // area.setTenantId();
        // area.setDeleteFlag();
        // area.setCreatedBy();
        // area.setUpdatedBy();
        // area.setCreatedTime();
        // area.setUpdatedTime();
        log.info("添加安防区域入库数据信息{}", JSONUtil.toJsonStr(area));
        videoAraeMapper.insert(area);
    }

    @Override
    public void updateVideoAreaById(UpdateVideoAreaDTO dto) {
        VideoArea videoArea = new VideoArea();
        BeanUtils.copyProperties(dto, videoArea);
        videoAraeMapper.updateById(videoArea);
    }

    @Override
    public VideoAreaVO getVideoAreaById(Long id) {
        VideoArea videoArea = videoAraeMapper.selectById(id);
        VideoAreaVO videoAreaVO = new VideoAreaVO();
        BeanUtils.copyProperties(videoArea, videoAreaVO);
        return videoAreaVO;
    }

    @Override
    public String removeVideoAreaById(Long id) {
        // 1.确认当前需要删除的安防区域有无下级安防区域
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(VideoArea::getAreaPids, "[" + id + "]");
        List<VideoArea> videoAreaChildren = videoAraeMapper.selectList(queryWrapper);
        if (videoAreaChildren.size() > 0) {
            return "不能删除含有下级安防区域的安防区域";
        }
        videoAraeMapper.deleteById(id);
        return "删除安防区域，操作成功";
    }

    @Override
    public void moveVideoArea(MoveVideoAreaDTO dto) {
        // 1.根据上级区域ID，获取上级信息
        VideoArea parentInfo = videoAraeMapper.selectById(dto.getAreaPid());
        // 2.根据id，查询当前节点信息
        VideoArea videoArea = videoAraeMapper.selectById(dto.getId());
        // 3.根据id，查询当前组织的直接下级组织信息
        List<VideoArea> childrenList = getChildren(dto.getId());
        // 4.更新当前节点信息
        videoArea.setAreaPid(parentInfo.getId());
        videoArea.setAreaPids(parentInfo.getAreaPids() + "[" + parentInfo.getId() + "]");
        videoArea.setLevel(parentInfo.getLevel() + 1);
        videoAraeMapper.updateById(videoArea);
        // 5.更新子节点信息
        if (childrenList.size() > 0) {
            for (VideoArea area : childrenList) {
                updateChildren(area, childrenList);
            }
        }

    }


    @Override
    public List<VideoAreaVO> getVideoAreaList() {
        List<VideoArea> videoAreaList = videoAraeMapper.selectList(null);
        return videoAreaList.stream().map(
                item -> {
                    VideoAreaVO videoAreaVO = new VideoAreaVO();
                    BeanUtils.copyProperties(item, videoAreaVO);
                    return videoAreaVO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<VideoAreaTree> getTreeList() {
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        List<VideoArea> videoList = videoAraeMapper.selectList(queryWrapper);
        List<VideoAreaTree> videoAreaTreeList = videoList.stream().map(
                item -> {
                    VideoAreaTree bean = new VideoAreaTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buiidTree(videoAreaTreeList, 1L);
    }

    private void updateChildren(VideoArea area, List<VideoArea> childrenList) {
        VideoArea parentInfo = videoAraeMapper.selectById(area.getAreaPid());
        for (VideoArea videoArea : childrenList) {
            videoArea.setAreaPids(parentInfo.getAreaPids() + "[" + area.getAreaPid() + "]");
            videoArea.setLevel(parentInfo.getLevel() + 1);
            videoAraeMapper.updateById(videoArea);
            List<VideoArea> sunChildrenList = getChildren(videoArea.getId());
            if (sunChildrenList.size() > 0) {
                for (VideoArea sun : sunChildrenList) {
                    updateChildren(sun,sunChildrenList);
                }
            }
        }
    }

    private List<VideoArea> getChildren(Long id) {
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VideoArea::getAreaPid, id);
        return videoAraeMapper.selectList(queryWrapper);
    }
}
