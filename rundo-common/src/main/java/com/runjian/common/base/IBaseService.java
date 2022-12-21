package com.runjian.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName IBaseService
 * @Description 通用业务接口
 * @date 2022-12-19 周一 18:04
 */
public interface IBaseService<T extends BaseEntity> extends IService<T> {
    /**
     * 新增
     *
     * @param domain 领域模型
     * @return {@code boolean}
     */
    boolean create(T domain);

    /**
     * 删除
     *
     * @param id {@code Long} ID
     * @return {@code boolean}
     */
    boolean remove(Long id);

    /**
     * 更新
     *
     * @param domain 领域模型
     * @return {@code boolean}
     */
    boolean update(T domain);

    /**
     * 获取
     *
     * @param id {@code Long} ID
     * @return 领域模型
     */
    T get(Long id);

    /**
     * 分页
     *
     * @param current {@code int} 页码
     * @param size    {@code int} 笔数
     * @param domain  领域模型
     * @return 管理员分页数据
     */
    IPage<?> page(int current, int size, T domain);
}
