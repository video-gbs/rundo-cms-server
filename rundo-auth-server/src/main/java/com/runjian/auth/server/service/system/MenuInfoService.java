package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.MenuInfo;
import com.runjian.auth.server.domain.vo.system.MenuInfoVO;
import com.runjian.auth.server.domain.vo.tree.MenuInfoTree;

import java.util.List;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface MenuInfoService extends IService<MenuInfo> {

    List<MenuInfoTree> findByTree(QuerySysMenuInfoDTO dto);

    void save(SysMenuInfoDTO dto);

    void modifyById(SysMenuInfoDTO dto);

    MenuInfoVO findById(Long id);

    void erasureById(Long id);


    List<MenuInfoTree> findByTreeByAppType(Integer appType);

    void modifyByStatus(StatusChangeDTO dto);

    void modifyByHidden(HiddenChangeDTO dto);

    List<MenuInfoTree> getTreeByAppId(Long appId);
}
