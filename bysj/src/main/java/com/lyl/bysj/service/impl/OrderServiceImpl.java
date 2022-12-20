package com.lyl.bysj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.common.dto.orderDoctorDto;
import com.lyl.bysj.common.dto.orderUserDto;
import com.lyl.bysj.dao.orderDao;
import com.lyl.bysj.pojo.order;
import com.lyl.bysj.service.OrderService;
import com.lyl.bysj.utils.idParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<orderDao, order> implements OrderService {

    @Autowired
    private orderDao orderDao;

    @Override
    public List<orderDoctorDto> getDoctorOrders(Principal principal, LocalDate localDate) {
        List<orderDoctorDto> orderList = orderDao.getDoctorOrders(principal.getName(),localDate);
        orderList.forEach(order ->{
            order.setAge(idParse.age(order.getIdCard()));
            order.setSex(idParse.sex(order.getIdCard()));
        });
        return orderList;
    }

    @Override
    public orderDoctorDto getOrderById(Integer orderId) {
        orderDoctorDto o = orderDao.getOrderById(orderId);
        o.setAge(idParse.age(o.getIdCard()));
        o.setSex(idParse.sex(o.getIdCard()));
        return o;
    }

    @Override
    public Page<orderUserDto> getOrders(Page page, int userId,LocalDate date) {
        List<orderUserDto> o = orderDao.getOrders(page.getCurrent()-1,page.getSize(),userId,date);
        page.setTotal(o.size());
        page.setRecords(o);
        return page;
    }

    @Override
    public Page<orderUserDto> getSis(Page page, Integer userId, LocalDate date) {
        List<orderUserDto> o = orderDao.getOrdersofSis(page.getCurrent()-1,page.getSize(),userId,date);
        page.setTotal(o.size());
        page.setRecords(o);
        return page;
    }

    @Override
    public int countOrderToday() {
        int count = orderDao.getCountOrderToday();
        return count;
    }

    @Override
    public void removeAllNewOrder(Integer id) {
        orderDao.delOrdersAfterToday(id);
    }
}
