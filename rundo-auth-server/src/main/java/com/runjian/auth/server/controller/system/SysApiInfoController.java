package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.domain.dto.system.AddSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysApiInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysApiInfoTree;
import com.runjian.auth.server.service.system.SysApiInfoService;
import com.runjian.common.config.response.CommonResponse;
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
    public CommonResponse<?> save(@RequestBody AddSysApiInfoDTO dto) {
        log.info("添加接口信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysApiInfoService.saveSysApiInfo(dto);
        return CommonResponse.create(200, "操作成功", null);
    }

    @PostMapping("/update")
    @ApiOperation("编辑接口")
    public CommonResponse<?> updateSysDict(@RequestBody UpdateSysApiInfoDTO dto) {
        log.info("添加接口信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysApiInfoService.updateSysApiInfoById(dto);
        return CommonResponse.create(200, "操作成功", null);
    }

    @PostMapping("/status/change")
    @ApiOperation("接口状态切换")
    public CommonResponse<?> changeStatus(@RequestBody StatusSysApiInfoDTO dto) {
        log.info("接口状态切换前端传参{}", JSONUtil.toJsonStr(dto));
        sysApiInfoService.changeStatus(dto);
        return CommonResponse.create(200, "操作成功", null);
    }


    @GetMapping("/getById/{id}")
    @ApiOperation("获取接口信息")
    public CommonResponse<SysApiInfoVO> getById(@PathVariable Long id) {
        return CommonResponse.create(200, "操作成功", sysApiInfoService.getSysApiInfoById(id));
    }

    @PostMapping("/tree")
    @ApiOperation("获取接口层级树")
    public CommonResponse<List<SysApiInfoTree>> getApiInfoTree(@RequestBody QuerySysApiInfoDTO dto) {
        log.info("获取接口层级树，前端查询条件:{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.create(200, "操作成功", sysApiInfoService.getSysApiInfoTree(dto));
    }

    @GetMapping("/getList")
    @ApiOperation("获取接口列表 无分页")
    public CommonResponse<List<SysApiInfoVO>> getList() {
        return CommonResponse.create(200, "操作成功", sysApiInfoService.getSysApiInfoList());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("获取接口列表 分页")
    public CommonResponse<IPage<SysApiInfoVO>> getListByPage(@RequestParam(value = "pageNum", defaultValue = "1")
                                                             Integer pageNum,
                                                             @RequestParam(value = "pageSize", defaultValue = "20")
                                                             Integer pageSize) {
        return CommonResponse.create(200, "操作成功", sysApiInfoService.getSysApiInfoByPage(pageNum, pageSize));
    }
}
