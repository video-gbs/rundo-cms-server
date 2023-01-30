package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.model.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.model.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.model.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.entity.system.SysMenuInfo;

import java.util.List;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysMenuInfoService extends IService<SysMenuInfo> {

    void addSysMenu(AddSysMenuInfoDTO dto);

    ResponseResult updateSysMenu(AddSysMenuInfoDTO dto);

    ResponseResult<List<SysMenuInfoVO>> sysMenuInfoList();

    void updateSysMenuInfoById(UpdateSysMenuInfoDTO dto);

    SysMenuInfoVO getSysMenuInfoById(Long id);

    List<SysMenuInfoVO> getSysMenuInfoList();

}
