package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.domain.dto.system.AddSysConfigDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysConfigDTO;
import com.runjian.auth.server.domain.vo.system.SysConfigVO;
import com.runjian.auth.server.service.system.ConfigInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统全局参数配置 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "系统参数配置")
@RestController
@RequestMapping("/sysConfig")
public class ConfigInfoController {

    @Autowired
    private ConfigInfoService configInfoService;

    @PostMapping("/add")
    @ApiOperation("添加系统参数配置")
    public CommonResponse<?> add(@RequestBody AddSysConfigDTO dto) {
        configInfoService.save(dto);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    @ApiOperation("编辑系统参数配置")
    public CommonResponse<?> update(@RequestBody UpdateSysConfigDTO dto) {
        configInfoService.modifyById(dto);
        return CommonResponse.success();
    }

    @GetMapping("/getById")
    @ApiOperation("获取编辑系统参数配置信息")
    public CommonResponse<SysConfigVO> getById(@RequestParam Long id) {
        return CommonResponse.success(configInfoService.findById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取系统参数配置列表 无分页")
    public CommonResponse<List<SysConfigVO>> getList() {
        return CommonResponse.success(configInfoService.findByList());
    }

}