package com.runjian.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName BaseController
 * @Description 通用请求处理
 * @date 2022-12-19 周一 18:02
 */
@SuppressWarnings("all")
public class BaseController<T extends BaseEntity, S extends IBaseService<T>> {

    @Resource
    protected HttpServletRequest request;

    @Autowired
    protected S service;

    /**
     * 新增
     *
     * @param domain 领域模型
     * @return {@link CommonResponse}
     */
    @PostMapping("create")
    public CommonResponse create(@Valid @RequestBody T domain) {
        // 业务逻辑
        boolean created = service.create(domain);
        if (created) {
            return CommonResponse.success("创建成功");
        }

        return CommonResponse.failure(BusinessErrorEnums.INTERFACE_ADDRESS_INVALID);
    }

    /**
     * 删除
     *
     * @param id {@code Long}
     * @return {@link CommonResponse}
     */
    @DeleteMapping("remove/{id}")
    public CommonResponse remove(@PathVariable Long id) {
        // 业务逻辑
        boolean deleted = service.remove(id);
        if (deleted) {
            return CommonResponse.success("删除成功");
        }

        return CommonResponse.failure(BusinessErrorEnums.INTERFACE_ADDRESS_INVALID);
    }

    /**
     * 修改
     *
     * @param domain 领域模型
     * @return {@link CommonResponse}
     */
    @PutMapping("update")
    public CommonResponse update(@Valid @RequestBody T domain) {
        // 业务逻辑
        boolean updated = service.update(domain);
        if (updated) {
            return CommonResponse.success("编辑成功");
        }

        return CommonResponse.failure(BusinessErrorEnums.INTERFACE_ADDRESS_INVALID);
    }

    /**
     * 获取
     *
     * @param id {@code Long}
     * @return {@link CommonResponse}
     */
    @GetMapping("get/{id}")
    public CommonResponse get(@PathVariable Long id) {
        T domain = service.get(id);
        return CommonResponse.success(domain);
    }

    /**
     * 分页
     *
     * @param current {@code int} 页码
     * @param size    {@code int} 笔数
     * @return {@link CommonResponse}
     */
    @GetMapping("page")
    public CommonResponse page(
            @RequestParam int current, @RequestParam int size, @ModelAttribute T domain) {
        IPage<?> page = service.page(current, size, domain);
        return CommonResponse.success(page);
    }
}
