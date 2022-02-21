package com.lyl.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.dao.AdminDao;
import com.lyl.bysj.pojo.Admin;
import com.lyl.bysj.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao,Admin> implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public Admin login(String username, String password) {

        LambdaQueryWrapper<Admin> lqw = new LambdaQueryWrapper<Admin>();
        lqw.eq(Admin::getUsername,username);
        lqw.eq(Admin::getPassword,password);
        return adminDao.selectOne(lqw);
    }
}
