package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.pojo.Doctor;

public interface DoctorService extends IService<Doctor> {
    Doctor login(String username,String password);
}
