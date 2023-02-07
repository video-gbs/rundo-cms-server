package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.domain.entity.system.SysMenuInfo;
import com.runjian.auth.server.domain.vo.tree.SysMenuInfoTree;

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

    String removeSysMenuInfoById(Long id);

    List<SysMenuInfoTree> getSysOrgTree(QuerySysMenuInfoDTO dto);
}
