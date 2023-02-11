package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.entity.MenuInfo;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
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
public interface SysMenuInfoService extends IService<MenuInfo> {

    List<SysMenuInfoTree> getSysMenuTree(QuerySysMenuInfoDTO dto);

    void addSysMenu(AddSysMenuInfoDTO dto);

    void updateSysMenuInfoById(UpdateSysMenuInfoDTO dto);

    SysMenuInfoVO getSysMenuInfoById(Long id);

    List<SysMenuInfoVO> getSysMenuInfoList();

    void removeSysMenuInfoById(Long id);


}
