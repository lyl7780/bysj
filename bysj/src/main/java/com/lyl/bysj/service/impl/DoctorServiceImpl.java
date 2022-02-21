package com.lyl.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.dao.DoctorDao;
import com.lyl.bysj.pojo.Admin;
import com.lyl.bysj.pojo.Doctor;
import com.lyl.bysj.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorDao, Doctor> implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;


    @Override
    public Doctor login(String username, String password) {
        LambdaQueryWrapper<Doctor> lqw = new LambdaQueryWrapper<Doctor>();
        lqw.eq(Doctor::getUsername,username);
        lqw.eq(Doctor::getPassword,password);
        return doctorDao.selectOne(lqw);
    }
}
