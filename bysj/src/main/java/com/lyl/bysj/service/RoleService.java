package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.pojo.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    Long getCountByRole(String role);
}
