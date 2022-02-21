package com.lyl.bysj.controller.utils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* 作为SpringMVC的异常处理器
* */
@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler
    public result doException(Exception e){
        e.printStackTrace();
        return new result(false,"服务器故障，请联系管理员！");
    }
}
