package com.lyl.bysj.controller;

import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //根据日期和科室查看可预约医生
    @GetMapping("/{date}/{officeId}")
    public result viewAttend(@PathVariable String date,@PathVariable int officeId){
        return  result.success(userService.viewAttend(date,officeId));
    }
}
