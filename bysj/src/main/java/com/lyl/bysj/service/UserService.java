package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.common.dto.DoctorDto;
import com.lyl.bysj.common.dto.DoctorFormDto;
import com.lyl.bysj.common.dto.UserDto;
import com.lyl.bysj.common.vo.UserVo;
import com.lyl.bysj.pojo.User;

import java.util.List;

public interface UserService extends IService<User> {
    User getByUsername(String username);
    String  getUserAuthoritiesInfo(int id);
    void clearUserAuthoritiesInfo(int userId);
    void clearUserAuthoritiesInfoByRoleId(int roleId);
    void clearUserAuthoritiesInfoByMenuId(int menuId);
    UserDto getByIdToDto(int id);
    Page<User> userListPage(Page page, String username,int type);
    Page<DoctorDto> doctorListPage(Page page,String name);
    DoctorFormDto doctorInfo(int id);
    void updateDoctor(DoctorFormDto dto);
    void saveDoctor(DoctorFormDto dto);

    void saveUser(UserVo userVo);
}
