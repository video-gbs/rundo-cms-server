package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.domain.dto.system.AddSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysAppInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;
import com.runjian.auth.server.service.system.SysAppInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 应用信息 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "应用管理")
@Slf4j
@RestController
@RequestMapping("/sysAppInfo")
public class SysAppInfoController {
    @Autowired
    SysAppInfoService sysAppInfoService;

    @PostMapping("/add")
    @ApiOperation("添加应用")
    public CommonResponse<?> save(@RequestBody AddSysAppInfoDTO dto) {
        log.info("添加应用信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysAppInfoService.saveSysAppInfo(dto);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    @ApiOperation("编辑应用")
    public CommonResponse<?> modify(@RequestBody UpdateSysAppInfoDTO dto) {
        log.info("编辑应用信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysAppInfoService.updateSysAppInfoById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除应用")
    public CommonResponse<?> remove(@PathVariable Long id) {
        log.info("删除应用信息前端传参{}", id);
        sysAppInfoService.removeSysAppInfoById(id);
        return CommonResponse.success();
    }

    @PostMapping("/status/change")
    @ApiOperation("应用状态切换")
    public CommonResponse<?> changeStatus(@RequestBody StatusSysAppInfoDTO dto) {
        log.info("应用状态切换前端传参{}", JSONUtil.toJsonStr(dto));
        sysAppInfoService.changeStatus(dto);
        return CommonResponse.success();
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取应用信息")
    public CommonResponse<SysAppInfoVO> getById(@PathVariable Long id) {
        return CommonResponse.success(sysAppInfoService.getSysAppInfoById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取应用列表 无分页")
    public CommonResponse<List<SysAppInfoVO>> getList() {
        return CommonResponse.success(sysAppInfoService.getSysAppInfoList());
    }

    @PostMapping("/getListByPage")
    @ApiOperation("获取应用列表 分页")
    public CommonResponse<IPage<SysAppInfoVO>> getListByPage(@RequestBody QuerySysAppInfoDTO dto) {
        return CommonResponse.success(sysAppInfoService.getSysAppInfoByPage(dto));
    }


}
