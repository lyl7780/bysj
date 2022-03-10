package com.lyl.bysj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyl.bysj.common.dto.AttendDto;
import com.lyl.bysj.pojo.attend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface attendDao extends BaseMapper<attend> {
    List<AttendDto> getAttends(@Param("current") long current, @Param("size") long size, @Param("date") LocalDate date, @Param("username") String username);

    List<AttendDto> userGetAttends(@Param("current") long current, @Param("size") long size, @Param("date") LocalDate date, @Param("officeId") int officeId, @Param("username") String username);
}
