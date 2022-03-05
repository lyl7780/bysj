package com.lyl.bysj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.dao.DepartmentDoctorDao;
import com.lyl.bysj.pojo.departmentDoctor;
import com.lyl.bysj.service.DepartmentDoctorService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentDoctorServiceImpl extends ServiceImpl<DepartmentDoctorDao, departmentDoctor> implements DepartmentDoctorService {
}
