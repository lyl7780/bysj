package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.pojo.Admin;

public interface AdminService extends IService<Admin> {
    Admin login(String username,String password);
}
