package com.lyl.bysj.controller;

import com.lyl.bysj.service.UserService;
import com.lyl.bysj.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    HttpServletRequest req;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    protected UserService userService;
}
