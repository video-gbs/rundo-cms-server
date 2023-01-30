package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysApiInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.service.system.SysApiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 接口信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "接口管理")
@Slf4j
@RestController
@RequestMapping("/sysApiInfo")
public class SysApiInfoController {

    @Autowired
    private SysApiInfoService sysApiInfoService;

    @PostMapping("/add")
    @ApiOperation("添加接口")
    public ResponseResult<?> save(@RequestBody AddSysApiInfoDTO dto) {
        log.info("添加接口信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysApiInfoService.saveSysApiInfo(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑接口")
    public ResponseResult<?> updateSysDict(@RequestBody UpdateSysApiInfoDTO dto) {
        log.info("添加接口信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysApiInfoService.updateSysApiInfoById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取接口信息")
    public ResponseResult<SysApiInfoVO> getById(@RequestParam Long id) {
        return new ResponseResult<>(200, "操作成功", sysApiInfoService.getSysApiInfoById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取接口列表 无分页")
    public ResponseResult<List<SysApiInfoVO>> getList() {
        return new ResponseResult<>(200, "操作成功", sysApiInfoService.getSysApiInfoList());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("获取接口列表 分页")
    public ResponseResult<IPage<SysApiInfoVO>> getListByPage(@RequestParam(value = "pageNum", defaultValue = "1")
                                                             Integer pageNum,
                                                             @RequestParam(value = "pageSize", defaultValue = "20")
                                                             Integer pageSize) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", sysApiInfoService.getSysApiInfoByPage(pageNum, pageSize));
    }
}
