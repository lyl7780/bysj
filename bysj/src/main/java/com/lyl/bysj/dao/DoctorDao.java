package com.lyl.bysj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyl.bysj.pojo.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DoctorDao extends BaseMapper<Doctor> {
}
