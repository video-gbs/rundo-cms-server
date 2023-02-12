package com.runjian.auth.server.service.system.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddVideoAreaDTO;
import com.runjian.auth.server.domain.dto.system.UpdateVideoAreaDTO;
import com.runjian.auth.server.domain.entity.VideoArea;
import com.runjian.auth.server.domain.vo.tree.VideoAreaTree;
import com.runjian.auth.server.domain.dto.system.MoveVideoAreaDTO;
import com.runjian.auth.server.domain.vo.system.VideoAreaVO;
import com.runjian.auth.server.mapper.VideoAraeMapper;
import com.runjian.auth.server.service.system.VideoAreaSaervice;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import com.runjian.common.config.exception.BusinessException;
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
    public VideoAreaVO save(AddVideoAreaDTO dto) {
        VideoArea area = new VideoArea();
        area.setAreaName(dto.getAreaName());
        area.setAreaPid(dto.getAreaPid());
        VideoArea prentInfo = videoAraeMapper.selectById(dto.getAreaPid());
        String pids = prentInfo.getAreaPids() + "[" + dto.getAreaPid() + "]";
        String areaNames = prentInfo.getAreaNames() + "/" + dto.getAreaName();
        area.setAreaPids(pids);
        area.setAreaNames(areaNames);
        area.setDescription(dto.getDescription());
        area.setLevel(prentInfo.getLevel() + 1);
        log.info("添加安防区域入库数据信息{}", JSONUtil.toJsonStr(area));
        videoAraeMapper.insert(area);
        VideoAreaVO videoAreaVO = new VideoAreaVO();
        BeanUtils.copyProperties(area, videoAreaVO);
        return videoAreaVO;
    }

    @Override
    public void modifyById(UpdateVideoAreaDTO dto) {
        VideoArea videoArea = new VideoArea();
        BeanUtils.copyProperties(dto, videoArea);
        videoAraeMapper.updateById(videoArea);
    }

    @Override
    public VideoAreaVO findById(Long id) {
        VideoArea videoArea = videoAraeMapper.selectById(id);
        VideoAreaVO videoAreaVO = new VideoAreaVO();
        BeanUtils.copyProperties(videoArea, videoAreaVO);
        return videoAreaVO;
    }

    @Override
    public String erasureById(Long id) {
        // 1.判断是否为根节点
        VideoArea videoArea = videoAraeMapper.selectById(id);
        if (videoArea.getAreaPid().equals(0L)) {
            throw new BusinessException("系统内置根节点不能删除");
        }
        // 2.确认当前需要删除的安防区域有无下级安防区域
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(VideoArea::getAreaPids, "[" + videoArea.getAreaPid() + "]");
        List<VideoArea> videoAreaChildren = videoAraeMapper.selectList(queryWrapper);
        // 3.删除子代节点
        for (VideoArea area : videoAreaChildren) {
            // 调用远端确认是否可以删除
            videoAraeMapper.deleteById(area.getId());
        }
        // 4.删除目标节点
        // 调用远端确认是否可以删除
        videoAraeMapper.deleteById(id);
        return "删除安防区域，操作成功";
    }

    @Override
    public void moveVideoArea(MoveVideoAreaDTO dto) {
        //  0-1 禁止本级移动到本级
        if (dto.getId().equals(dto.getAreaPid())) {
            return;
        }
        // 1.根据上级区域ID，获取上级信息
        VideoArea parentInfo = videoAraeMapper.selectById(dto.getAreaPid());
        VideoArea parentInfoId = videoAraeMapper.selectById(dto.getId());
        if (parentInfoId.getAreaPid().equals(parentInfo.getAreaPid())) {
            // 切换排序顺序
            parentInfoId.setAreaSort(parentInfo.getAreaSort());
            videoAraeMapper.updateById(parentInfo);
            parentInfo.setAreaSort(parentInfoId.getAreaSort());
            videoAraeMapper.updateById(parentInfo);
            return;
        }
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
    public List<VideoAreaVO> findByList(Long areaId) {
        // TODO 根据用户ID渲染
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        VideoArea videoArea = new VideoArea();
        if (areaId != null) {
            videoArea = videoAraeMapper.selectById(areaId);
            queryWrapper.likeRight(VideoArea::getAreaPids, videoArea.getAreaPids() + "[" + videoArea.getId() + "]");
        }
        List<VideoArea> videoAreaList = videoAraeMapper.selectList(queryWrapper);
        List<VideoAreaVO> videoAreaVOS = videoAreaList.stream().map(
                item -> {
                    VideoAreaVO videoAreaVO = new VideoAreaVO();
                    BeanUtils.copyProperties(item, videoAreaVO);
                    return videoAreaVO;
                }
        ).collect(Collectors.toList());
        VideoAreaVO videoAreaVO = new VideoAreaVO();
        BeanUtils.copyProperties(videoArea, videoAreaVO);
        videoAreaVOS.add(videoAreaVO);
        return videoAreaVOS;
    }

    @Override
    public String erasureBatch(List<Long> ids) {
        // 1.确定节点ID不为空
        if (ids.size() <= 0) {
            return "没有选定删除目标";
        }
        // 2.检索删除的节点中是否包含根节点
        List<VideoArea> videoAreaList = videoAraeMapper.selectBatchIds(ids);
        boolean flag = false;
        for (VideoArea area : videoAreaList) {
            if (area.getAreaPid().equals(0L)) {
                flag = true;
            }
        }
        if (flag) {
            return "删除目标中包含系统内置根节点";
        }
        videoAraeMapper.deleteBatchIds(ids);
        return "删除组织，操作成功!";
    }

    @Override
    public List<VideoAreaTree> findByTree() {
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderBy(true, true, VideoArea::getAreaSort, VideoArea::getUpdatedTime);
        List<VideoArea> videoList = videoAraeMapper.selectList(queryWrapper);
        List<VideoAreaTree> videoAreaTreeList = videoList.stream().map(
                item -> {
                    VideoAreaTree bean = new VideoAreaTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(videoAreaTreeList, 1L);
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
                    updateChildren(sun, sunChildrenList);
                }
            }
        }
    }

    private List<VideoArea> getChildren(Long id) {
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VideoArea::getAreaPid, id);
        queryWrapper.orderBy(true, true, VideoArea::getAreaSort, VideoArea::getUpdatedTime);
        return videoAraeMapper.selectList(queryWrapper);
    }
}
