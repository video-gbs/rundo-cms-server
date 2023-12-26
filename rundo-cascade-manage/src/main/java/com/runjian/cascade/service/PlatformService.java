package com.runjian.cascade.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.runjian.cascade.vo.response.GetPlatformPageRsp;

import java.util.Set;

/**
 * 级联上级服务
 * @author Miracle
 * @date 2023/12/11 17:57
 */
public interface PlatformService {

    /**
     * 获取平台分页数据
     * @param page
     * @param num
     * @param name 平台名称
     * @param ip IP地址
     * @return
     */
    PageInfo<GetPlatformPageRsp> getPlatformPage(int page, int num, String name, String ip);

    /**
     * 添加平台
     * @param name 平台名称
     * @param gbCode 国标编码
     * @param ip IP地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     */
    void addPlatform(String name, String gbCode, String ip, Integer port, String username, String password);

    /**
     * 修改平台
     * @param platformId 平台id
     * @param name 平台名称
     * @param gbCode 国标编码
     * @param ip IP地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     */
    void updatePlatform(Long platformId, String name, String gbCode, String ip, Integer port, String username, String password);

    /**
     * 批量删除平台
     * @param platformIds 平台id数组
     */
    void batchDeletePlatform(Set<Long> platformIds);


}
