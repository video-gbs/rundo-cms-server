package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.common.CommonPage;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysAppInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;
import com.runjian.auth.server.service.system.SysAppInfoService;
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
    public ResponseResult<?> save(@RequestBody AddSysAppInfoDTO dto) {
        log.info("添加应用信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysAppInfoService.saveSysAppInfo(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑应用")
    public ResponseResult<?> modify(@RequestBody UpdateSysAppInfoDTO dto) {
        log.info("编辑应用信息前端传参{}", JSONUtil.toJsonStr(dto));
        sysAppInfoService.updateSysAppInfoById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/remove")
    @ApiOperation("删除应用")
    public ResponseResult<?> remove(@RequestParam Long id) {
        log.info("删除应用信息前端传参{}", id);
        sysAppInfoService.removeSysAppInfoById(id);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取应用信息")
    public ResponseResult<SysAppInfoVO> getById(@RequestParam Long id) {
        return new ResponseResult<>(200, "操作成功", sysAppInfoService.getSysAppInfoById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取应用列表 无分页")
    public ResponseResult<List<SysAppInfoVO>> getList() {
        return new ResponseResult<>(200, "操作成功", sysAppInfoService.getSysAppInfoList());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("获取应用列表 分页")
    public ResponseResult<CommonPage<SysAppInfoVO>> getListByPage(@RequestParam(value = "pageNum", defaultValue = "1")
                                                                  Integer pageNum,
                                                                  @RequestParam(value = "pageSize", defaultValue = "20")
                                                                  Integer pageSize) {

        Page<SysAppInfoVO> sysAppInfoVOPage = sysAppInfoService.getSysAppInfoByPage(pageNum, pageSize);
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", CommonPage.restPage(sysAppInfoVOPage));
    }


}
