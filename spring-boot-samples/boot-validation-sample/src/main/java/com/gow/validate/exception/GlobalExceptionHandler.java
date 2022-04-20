package com.gow.validate.exception;

import com.gow.validate.pojo.common.BusinessCode;
import com.gow.validate.pojo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * @author wujt  2021/5/18
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数校验失败", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return Result.fail(BusinessCode.parameter_verification_failed, msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("参数校验失败", ex);
        return Result.fail(BusinessCode.parameter_verification_failed, ex.getMessage());
    }

    @ExceptionHandler({NotReadablePropertyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleNotReadablePropertyException(NotReadablePropertyException ex) {
        log.error("参数校验失败", ex);
        return Result.fail(BusinessCode.parameter_verification_failed, ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result onMethodArgumentTypeMismatchException(HttpServletRequest request, HttpServletResponse response, MethodArgumentTypeMismatchException exception) {
        log.error("method failed. Method Argument Type Mismatch Exception occur!, message = {}", exception.getMessage(), exception);
        String exceptionMsg = "请检查" + exception.getName() + "参数值是否在其预计范围和类型内";
        return Result.fail(BusinessCode.parameter_type_does_not_match, exceptionMsg);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result onHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException exception) {
        log.error("method failed. Missing Required HTTP Body!, message = {}", exception.getMessage());
        return Result.fail(BusinessCode.unknown_system_error, "请检查body数据是否在其预计范围和类型内");
    }



    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result onHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
    log.error("method failed. Http Request Method Not Supported Exception occur!, message = {}", exception.getMessage(), exception);
        return Result.fail(BusinessCode.resource_does_not_exist, exception.getMessage());
    }
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result illegalArgumentException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
       log.error("method failed. Illegal Argument Exception occur!, message = {}", exception.getMessage(), exception);
        return Result.fail(BusinessCode.parameter_verification_failed, exception.getMessage());
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleException(Exception ex) {
        log.error("未知系统错误", ex);
        return Result.fail(BusinessCode.unknown_system_error, ex.getMessage());
    }


}
