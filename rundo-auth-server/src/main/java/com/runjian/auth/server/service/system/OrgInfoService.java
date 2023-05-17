package com.runjian.auth.server.service.system;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.MoveSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.SysOrgDTO;
import com.runjian.auth.server.domain.entity.OrgInfo;
import com.runjian.auth.server.domain.vo.system.OrgInfoVO;
import com.runjian.auth.server.domain.vo.system.SysOrgVO;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
import com.runjian.common.config.response.CommonResponse;

import java.util.List;

/**
 * <p>
 * 组织机构表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface OrgInfoService extends IService<OrgInfo> {

    SysOrgVO save(SysOrgDTO dto);

    List<Tree<Long>> findByTree();

    IPage<OrgInfo> findByPage(Integer pageNum, Integer pageSize);

    CommonResponse erasureById(Long id);

    void modifyById(SysOrgDTO dto);

    SysOrgVO findById(Long id);

    List<SysOrgVO> findByList();

    void moveSysOrg(MoveSysOrgDTO dto);

    List<OrgInfo> getOrgInfoByRoleCode(String roleCode);

    List<Long> getOrgIdListByRoleId(Long roleId);

    OrgInfoVO getOrgInfoByUserId(Long id);
}
