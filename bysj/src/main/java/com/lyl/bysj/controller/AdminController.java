package com.lyl.bysj.controller;

import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController{


    @PostMapping("/login")
    public result login(String username,String password){
        User user = userService.adminLogin(username,password);
        if(user == null){
            return result.fail("用户名或密码不正确");
        }
        return result.success(user);
    }

}
