package com.lyl.bysj.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyl.bysj.common.dto.DoctorDto;
import com.lyl.bysj.common.dto.DoctorFormDto;
import com.lyl.bysj.common.vo.UserVo;
import com.lyl.bysj.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao extends BaseMapper<User> {

    User selectOneByUsernameAndPasswordFromAdmin(@Param("username") String username,@Param("password") String password);
    List<Integer> getNavMenuIds(@Param("id") int userId);
    List<User> listByMenuId(@Param("id") int menuId);
    List<DoctorDto> doctorList(@Param("current") Long current,@Param("size") Long size,@Param("name") String name);
    DoctorFormDto doctorInfo(int id);

    void insertUser(@Param("userVo") UserVo userVo);
}
