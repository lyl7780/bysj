package com.lyl.bysj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.dao.departmentDao;
import com.lyl.bysj.pojo.department;
import com.lyl.bysj.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<departmentDao, department> implements DepartmentService {
}
