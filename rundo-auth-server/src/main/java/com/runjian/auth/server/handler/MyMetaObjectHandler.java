// package com.runjian.auth.server.handler;
//
// import cn.hutool.core.date.DateUtil;
// import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.ibatis.reflection.MetaObject;
// import org.springframework.stereotype.Component;
//
// /**
//  * @author Jiang4Yu
//  * @version V1.0.0
//  * @ClassName MyMetaObjectHandler
//  * @Description 自动填充
//  * @date 2023-01-28 周六 18:13
//  */
//
// @Slf4j
// @Component
// public class MyMetaObjectHandler implements MetaObjectHandler {
//     @Override
//     public void insertFill(MetaObject metaObject) {
//         log.info("MybatisPlus 添加时自动参数填充");
//         this.setFieldValByName("created_time", DateUtil.now(), metaObject);
//         this.setFieldValByName("updated_time", DateUtil.now(), metaObject);
//
//     }
//
//     @Override
//     public void updateFill(MetaObject metaObject) {
//         log.info("MybatisPlus 添加时自动参数填充");
//         this.setFieldValByName("updated_time", DateUtil.now(), metaObject);
//     }
// }
