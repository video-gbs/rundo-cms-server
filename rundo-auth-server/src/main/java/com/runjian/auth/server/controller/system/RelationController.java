package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.domain.dto.system.QueryRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.service.system.UserInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RelationController
 * @Description 关联用户相关
 * @date 2023-03-13 周一 9:19
 */
@Api(tags = "用户管理")
@Slf4j
@RestController
@RequestMapping("/sysUserInfo")
public class RelationController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/getRelationSysUserInfo/{id}")
    @ApiOperation("关联用户查看单个用户信息")
    public CommonResponse<RelationSysUserInfoVO> getRelationSysUserInfoById(@PathVariable Long id) {
        return CommonResponse.success(userInfoService.findRelationById(id));
    }

    @PostMapping("/getRelationSysUserInfoList")
    @ApiOperation("关联用户用户列表")
    public CommonResponse<IPage<RelationSysUserInfoVO>> getRelationSysUserInfoList(@RequestBody QueryRelationSysUserInfoDTO dto) {
        log.info("关联用户用户列表前端传参{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.success(userInfoService.findRelationList(dto));
    }

}
