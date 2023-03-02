package com.runjian.common.config.exception;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2020/2/19 15:10
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CommonResponse doError(HttpServletResponse response, Exception ex) {
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "业务异常", businessException.getErrDetail(), businessException.getBusinessErrorEnums());
            response.setStatus(businessException.getState());
            return CommonResponse.failure(businessException.getBusinessErrorEnums(), businessException.getErrDetail());
        } else if (ex instanceof MethodArgumentNotValidException) {
            // 方法请求参数绑定异常
            MethodArgumentNotValidException srEx = (MethodArgumentNotValidException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "MethodArgumentNotValidException异常", null, srEx);
            List<String> defaultMsg = srEx.getBindingResult().getFieldErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            response.setStatus(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR.getState());
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, defaultMsg.get(0));
        } else if (ex instanceof BindException) {
            // 请求参数绑定异常
            BindException srEx = (BindException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "BindException异常", null, srEx);
            List<String> defaultMsg = srEx.getBindingResult().getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            response.setStatus(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR.getState());
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, defaultMsg.get(0));
        } else if (ex instanceof MissingServletRequestParameterException) {
            // 请求参数绑定异常
            MissingServletRequestParameterException srEx = (MissingServletRequestParameterException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "MissingServletRequestParameterException异常", null, srEx);
            response.setStatus(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR.getState());
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, srEx.getMessage());
        } else if (ex instanceof ConstraintViolationException) {
            // 请求参数绑定异常
            ConstraintViolationException srEx = (ConstraintViolationException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "MissingServletRequestParameterException异常", null, srEx);
            List<String> defaultMsg = srEx.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            response.setStatus(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR.getState());
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, defaultMsg.get(0));
        } else if (ex instanceof HttpMessageNotReadableException) {
            // 请求参数绑定异常
            HttpMessageNotReadableException srEx = (HttpMessageNotReadableException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "HttpMessageNotReadableException异常", null, srEx);
            response.setStatus(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR.getState());
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, srEx.getMessage());
        } else if (ex instanceof NotLoginException) {
            // 未登录异常
            NotLoginException srEx = (NotLoginException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "BindException异常", null, srEx);
            response.setStatus(BusinessErrorEnums.USER_LOGIN_FAILURE.getState());
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, srEx.getMessage());
        } else if (ex instanceof NotRoleException) {
            // 角色异常
            NotRoleException srEx = (NotRoleException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "BindException异常", null, srEx);
            response.setStatus(BusinessErrorEnums.ROLE_NOT_FOUND.getState());
            return CommonResponse.failure(BusinessErrorEnums.ROLE_NOT_FOUND, srEx.getMessage());
        } else if (ex instanceof NotPermissionException) {
            // 权限异常
            NotPermissionException srEx = (NotPermissionException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "BindException异常", null, srEx);
            response.setStatus(BusinessErrorEnums.ROLE_NOT_FOUND.getState());
            return CommonResponse.failure(BusinessErrorEnums.ROLE_NOT_FOUND, srEx.getMessage());
        } else if (ex instanceof DisableServiceException) {
            // 封禁异常
            DisableServiceException srEx = (DisableServiceException) ex;
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "BindException异常", null, srEx);
            response.setStatus(BusinessErrorEnums.USER_ACCOUNT_NOT_ENABLED.getState());
            return CommonResponse.failure(BusinessErrorEnums.USER_ACCOUNT_NOT_ENABLED, srEx.getMessage());
        } else {
            // 其他异常
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "全局异常捕获", "未知异常", null, ex);
            response.setStatus(BusinessErrorEnums.UNKNOWN_ERROR.getState());
            return CommonResponse.failure(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        }
    }
}
