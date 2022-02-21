package com.lyl.bysj.controller;

import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.Admin;
import com.lyl.bysj.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;


    @GetMapping("/{username}/{password}")
    public result login(@PathVariable String username, @PathVariable String password, HttpServletRequest request){
        Admin admin = adminService.login(username,password);
        boolean flag = (admin != null);
        if(flag){
            //登录成功，保存数据
            HttpSession session = request.getSession();
            session.setAttribute("admin",admin.getAdminId());
        }
        return new result(flag,admin);
    }
}