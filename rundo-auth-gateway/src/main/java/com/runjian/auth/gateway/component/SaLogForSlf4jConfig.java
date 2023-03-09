package com.runjian.auth.gateway.component;

import cn.dev33.satoken.log.SaLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SaLogForSlf4jConfig
 * @Description 将 Sa-Token 的日志信息转接到 Slf4j
 * @date 2023-03-09 周四 11:05
 */
@Slf4j
@Component
public class SaLogForSlf4jConfig implements SaLog {
    @Override
    public void trace(String str, Object... args) {
        log.trace(str, args);
    }

    @Override
    public void debug(String str, Object... args) {
        log.debug(str, args);
    }

    @Override
    public void info(String str, Object... args) {
        log.info(str, args);
    }

    @Override
    public void warn(String str, Object... args) {
        log.warn(str, args);
    }

    @Override
    public void error(String str, Object... args) {
        log.error(str, args);
    }

    @Override
    public void fatal(String str, Object... args) {
        log.error(str, args);
    }
}
