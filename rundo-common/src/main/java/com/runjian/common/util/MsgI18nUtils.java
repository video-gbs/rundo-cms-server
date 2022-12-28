package com.runjian.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName MsgI18nUtils
 * @Description i18n 工具类
 * @date 2022-12-28 周三 15:51
 */
@UtilityClass
public class MsgI18nUtils {

    /**
     * 通过code 获取中文错误信息
     *
     * @param code
     * @return
     */
    public String getMessage(String code) {
        MessageSource messageSource = SpringContextHolder.getBean("messageSource");
        return messageSource.getMessage(code, null, Locale.CHINA);
    }

    /**
     * 通过code 和参数获取中文错误信息
     *
     * @param code
     * @return
     */
    public String getMessage(String code, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("messageSource");
        return messageSource.getMessage(code, objects, Locale.CHINA);
    }

    /**
     * security 通过code 和参数获取中文错误信息
     *
     * @param code
     * @return
     */
    public String getSecurityMessage(String code, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("securityMessageSource");
        return messageSource.getMessage(code, objects, Locale.CHINA);
    }

}
