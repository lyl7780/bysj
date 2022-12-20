package com.lyl.bysj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyl.bysj.common.dto.orderDoctorDto;
import com.lyl.bysj.common.dto.orderUserDto;
import com.lyl.bysj.pojo.order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface orderDao extends BaseMapper<order> {
    List<orderDoctorDto> getDoctorOrders(@Param("doctorName") String doctorName, @Param("localDate") LocalDate localDate);

    orderDoctorDto getOrderById(@Param("orderId") Integer orderId);

    List<orderUserDto> getOrders(@Param("current") long current, @Param("size") long size, @Param("userId") int userId, @Param("date") LocalDate date);

    List<orderUserDto> getOrdersofSis(@Param("current") long current, @Param("size") long size, @Param("userId") Integer userId, @Param("date") LocalDate date);

    int getCountOrderToday();

    void delOrdersAfterToday(@Param("id") Integer id);
}
