package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.pojo.attendDoctor;

import java.util.List;

public interface UserService extends IService<User> {
    User login(String username, String password);
    List<attendDoctor> viewAttend(String date,int id);
}
