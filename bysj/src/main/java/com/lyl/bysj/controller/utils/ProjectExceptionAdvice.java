package com.lyl.bysj.controller.utils;

import cn.hutool.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* 作为SpringMVC的异常处理器
* */
@Slf4j
@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public result doException(MethodArgumentNotValidException e){
        BindingResult Result = e.getBindingResult();
        ObjectError objectError = Result.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常->{}",objectError.getDefaultMessage());
        return result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = AccessDeniedException.class)
    public result doException(AccessDeniedException e){
        log.error("权限禁止->{}",e.getMessage());
        return result.fail(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public result doException(Exception e){
        e.printStackTrace();
        return result.fail("服务器故障，请联系管理员！");
    }
}
