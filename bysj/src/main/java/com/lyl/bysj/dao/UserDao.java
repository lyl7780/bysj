package com.lyl.bysj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.pojo.attendDoctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao extends BaseMapper<User> {

    List<attendDoctor> selectByDateAndOffice(@Param("date") String date, @Param("id") int officeId);
    User selectOneByUsernameAndPasswordFromAdmin(@Param("username") String username,@Param("password") String password);
}
