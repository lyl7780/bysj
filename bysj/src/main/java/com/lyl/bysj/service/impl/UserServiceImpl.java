package com.lyl.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.dao.UserDao;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.pojo.attendDoctor;
import com.lyl.bysj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.eq(User::getUsername,username);
        lqw.eq(User::getPassword,password);
        return userDao.selectOne(lqw);
    }

    @Override
    public List<attendDoctor> viewAttend(String date, int officeId) {
        return userDao.selectByDateAndOffice(date,officeId);
    }

    @Override
    public User adminLogin(String username, String password) {
        return userDao.selectOneByUsernameAndPasswordFromAdmin(username,password);
    }
}
