package com.gow.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gow.common.CommonCode;
import com.gow.common.Result;
import com.gow.spring.i18n.MessageResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.i18n.LocaleContextHolder;
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
import java.util.Locale;
import java.util.Optional;

/**
 * @author wujt  2021/5/18
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@ConditionalOnProperty(prefix = "gow.exception", value = "enable", havingValue = "true")
public class GlobalExceptionHandler {

    @Autowired
    private MessageResourceService messageResourceService;

    @ExceptionHandler({CommonException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleCommonException(CommonException ex) {
        log.error("method failed, CommonException occur!, code = {}, message = {}", ex.getCode(), ex.getMessage(), ex);
        String message = messageResourceService.getMessage(ex.getCode());
        String tips = Optional.ofNullable(message).map(msg -> Optional.ofNullable(ex.getFields()).map(field -> {
            // 对获取的message进行field 处理
            String result = msg;
            try {
                JSONObject fields = JSON.parseObject(field);
                for (String key : fields.keySet()) {
                    String replaceKey = "{" + key + "}";
                    if (msg.contains(replaceKey)) {
                        result = result.replace(replaceKey, fields.getString(key));
                    }
                }
            } catch (Exception e) {
                log.error("error while process message", e);
            }
            return result;
        }).orElse(message))
                .orElseGet(() -> {
                    Locale locale = LocaleContextHolder.getLocale();
                    if (Locale.ENGLISH.getLanguage().equals(locale.getLanguage())) {
                        return "Server internal exception, please contact the administrator";
                    } else {
                        return "服务器内部异常，请联系管理员";
                    }
                });
        return new Result<>()
                .setCode(ex.getCode())
                .setMsg(tips)
                .setExceptionMsg(null);
    }


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
        return Result.fail(CommonCode.PARAMETER_TYPE_MISMATCH, msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("参数校验失败", ex);
        return Result.fail(CommonCode.PARAMETER_TYPE_MISMATCH, ex.getMessage());
    }

    @ExceptionHandler({NotReadablePropertyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleNotReadablePropertyException(NotReadablePropertyException ex) {
        log.error("参数校验失败", ex);
        return Result.fail(CommonCode.PARAMETER_TYPE_MISMATCH, ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result onMethodArgumentTypeMismatchException(HttpServletRequest request, HttpServletResponse response, MethodArgumentTypeMismatchException exception) {
        log.error("method failed. Method Argument Type Mismatch Exception occur!, message = {}", exception.getMessage(), exception);
        String exceptionMsg = "请检查" + exception.getName() + "参数值是否在其预计范围和类型内";
        return Result.fail(CommonCode.PARAMETER_TYPE_MISMATCH, exceptionMsg);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result onHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException exception) {
        log.error("method failed. Missing Required HTTP Body!, message = {}", exception.getMessage());
        return Result.fail(CommonCode.MISSING_REQUIRED_HTTP_BODY, "请检查body数据是否在其预计范围和类型内");
    }


    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result onHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        log.error("method failed. Http Request Method Not Supported Exception occur!, message = {}", exception.getMessage(), exception);
        return Result.fail(CommonCode.ILLEGAL_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result illegalArgumentException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        log.error("method failed. Illegal Argument Exception occur!, message = {}", exception.getMessage(), exception);
        return Result.fail(CommonCode.ILLEGAL_REQUEST, exception.getMessage());
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleException(Exception ex) {
        log.error("未知系统错误", ex);
        return Result.fail(CommonCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}
