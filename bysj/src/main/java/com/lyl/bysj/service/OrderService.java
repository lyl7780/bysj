package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.common.dto.orderDoctorDto;
import com.lyl.bysj.common.dto.orderUserDto;
import com.lyl.bysj.pojo.order;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface OrderService extends IService<order> {
    List<orderDoctorDto> getDoctorOrders(Principal principal, LocalDate localDate);

    orderDoctorDto getOrderById(Integer orderId);

    Page<orderUserDto> getOrders(Page page, int id,LocalDate date);

    Page<orderUserDto> getSis(Page page, Integer id, LocalDate convert);

    int countOrderToday();

    void removeAllNewOrder(Integer id);
}
