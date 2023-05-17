package com.runjian.auth.server.service.system;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.SysApiInfoDTO;
import com.runjian.auth.server.domain.entity.ApiInfo;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.domain.vo.tree.ApiInfoTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 接口信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface ApiInfoService extends IService<ApiInfo> {

    /**
     * 新增接口
     *
     * @param dto
     */
    void save(SysApiInfoDTO dto);

    /**
     * 修改接口
     *
     * @param dto
     */
    void modifyById(SysApiInfoDTO dto);

    /**
     * 通过ID获取接口信息
     *
     * @param id
     * @return
     */
    SysApiInfoVO findById(Long id);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<SysApiInfoVO> findByPage(Integer pageNum, Integer pageSize);

    /**
     * 切换接口状态
     *
     * @param dto
     */
    void modifyByStatus(StatusSysApiInfoDTO dto);

    /**
     * 树形方式展示接口
     *
     * @param dto
     * @return
     */
    List<Tree<Long>> findByTree(QuerySysApiInfoDTO dto);

    /**
     * 条件查询
     *
     * @param dto
     * @return
     */
    List<SysApiInfoVO> findByList(QuerySysApiInfoDTO dto);

    List<Tree<Long>> getTreeByAppId(Long appId);

    List<ApiInfo> getApiInfoByRoleCode(String roleCode);

    List<Long> getApiIdListByRoleId(@Param("roleId") Long roleId);
}
