package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.entity.system.SysOrg;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.model.vo.system.SysOrgNode;
import com.runjian.auth.server.service.system.SysOrgService;
import com.runjian.auth.server.util.RundoIdUtil;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织机构表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements SysOrgService {
    @Autowired
    private RundoIdUtil idUtis;

    @Autowired
    private SysOrgMapper sysOrgMapper;


    public List<SysOrgNode> getSysOrgTree(Long orgId, String orgName) {
        if (orgId != null) {
            List<SysOrg> orgList = sysOrgMapper.selectOrgTree(orgId, orgName);

            List<SysOrgNode> sysOrgNodeList = orgList.stream().map(
                    item -> {
                        SysOrgNode bean = new SysOrgNode();
                        BeanUtils.copyProperties(item, bean);
                        return bean;
                    }
            ).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(orgName)) {
                return sysOrgNodeList;
            } else {
                return DataTreeUtil.buiidTree(sysOrgNodeList, orgId);
            }

        } else {
            throw new RuntimeException("查询组织机构ID不能为空");
        }
    }

    @Override
    public IPage<SysOrg> getListByPage(Integer pageNum, Integer pageSize) {
        Page<SysOrg> page = Page.of(pageNum, pageSize);
        return sysOrgMapper.selectPage(page, null);
    }

}
