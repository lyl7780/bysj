package com.lyl.bysj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyl.bysj.service.*;
import com.lyl.bysj.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    protected HttpServletRequest req;
    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    protected UserService userService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected RoleMenuService roleMenuService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected UserRoleService userRoleService;
    @Autowired
    protected DepartmentService departmentService;
    @Autowired
    protected DepartmentDoctorService departmentDoctorService;


    /**
     * 获取页码
     * @return
     */
    public Page getPage(){
        int current = ServletRequestUtils.getIntParameter(req,"current",1);
        int size = ServletRequestUtils.getIntParameter(req,"size",10);
        return new Page(current,size);
    }
}
