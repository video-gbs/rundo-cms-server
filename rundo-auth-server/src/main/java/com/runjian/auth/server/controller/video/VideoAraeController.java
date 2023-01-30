package com.runjian.auth.server.controller.video;

import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.entity.video.VideoArea;
import com.runjian.auth.server.domain.dto.video.VideoAreaDTO;
import com.runjian.auth.server.domain.vo.video.AreaNode;
import com.runjian.auth.server.service.area.VideoAraeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 安防区域 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "安防区域")
@Slf4j
@RestController
@RequestMapping("/videoArae")
public class VideoAraeController {

    @Autowired
    private VideoAraeService videoAraeService;

    @PostMapping("/add")
    @ApiOperation("添加安防区域")
    public ResponseResult save(@RequestBody VideoAreaDTO dto) {
        log.info("添加安防区域前端传参信息{}", JSONUtil.toJsonStr(dto));
        videoAraeService.saveVideoArae(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/delete")
    @ApiOperation("删除安防区域")
    public ResponseResult delete(Long id) {
        // TODO 级联删除
        videoAraeService.removeById(id);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑安防区域")
    public ResponseResult update(@RequestBody VideoArea dto) {
        videoAraeService.updateById(dto);
        return new ResponseResult<>(200, "操作成功");
    }


    @GetMapping("/getById")
    @ApiOperation("获取安防区域信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", videoAraeService.getById(id));
    }

    @GetMapping("/getBaseTree")
    @ApiOperation("获取安防区域树,无需传参进行二次渲染")
    public ResponseResult<List<AreaNode>> getTreeList() {
        return new ResponseResult<>(200, "操作成功", videoAraeService.getTreeList());
    }

    @GetMapping("/getTree")
    @ApiOperation("获取安防区域树，需要传参进行二次渲染")
    public ResponseResult<List<AreaNode>> getTreeList(@Param("id") Long id, @Param("areaName") String areaName) {
        return new ResponseResult<>(200, "操作成功", videoAraeService.getTreeList(id, areaName));
    }


    @GetMapping("/getList")
    @ApiOperation("获取安防区域列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", videoAraeService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取安防区域列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", videoAraeService.list());
    }
}
