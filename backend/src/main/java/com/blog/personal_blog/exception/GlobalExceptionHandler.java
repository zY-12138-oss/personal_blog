package com.blog.personal_blog.exception;

import com.blog.personal_blog.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("Business exception: {}", e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null 
                ? e.getBindingResult().getFieldError().getDefaultMessage() 
                : "参数校验失败";
        log.error("Validation exception: {}", message);
        return R.error(400, message);
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldError() != null 
                ? e.getBindingResult().getFieldError().getDefaultMessage() 
                : "参数绑定失败";
        log.error("Bind exception: {}", message);
        return R.error(400, message);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("System exception: ", e);
        return R.error("系统异常，请联系管理员");
    }
}
