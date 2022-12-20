package com.lyl.bysj.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyl.bysj.common.dto.AttendDto;
import com.lyl.bysj.common.dto.MenuDto;
import com.lyl.bysj.common.dto.PassDto;
import com.lyl.bysj.common.dto.orderDoctorDto;
import com.lyl.bysj.controller.utils.Const;
import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.pojo.attend;
import com.lyl.bysj.pojo.order;
import com.lyl.bysj.utils.LocalDateConverter;
import com.lyl.bysj.utils.RedisUtil;
import com.lyl.bysj.utils.Times;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@PreAuthorize("hasRole('DOCTOR')")
@RestController
@RequestMapping("/doctor")
public class DoctorController extends BaseController{
    /**
     * 获取当前用户菜单和权限信息
     * @param principal
     * @return
     */
    @GetMapping("/menu/nav")
    public result nav(Principal principal){
        return getNav(principal);
    }

    /**
     * 获取当前用户信息
     * @param principal
     * @return
     */
    @GetMapping("/userInfo")
    public result userInfo(Principal principal){
        return result.success(userService.getByUsername(principal.getName()));
    }

    /**
     * 医生改密码
     * @param passDto
     * @param principal
     * @return
     */
    @PostMapping("/resetPassword")
    public result resetPassword(@Validated @RequestBody PassDto passDto, Principal principal){
        return rstPassword(passDto,principal);
    }

    /**
     * 查询出勤信息
     * @param date
     * @param principal
     * @return
     */
    @GetMapping("/attend/list")
    public result attendList(String date,Principal principal){
        Page<AttendDto> attend = attendService.getAttends(getPage(), new LocalDateConverter("yyyy-MM-dd").convert(date),principal.getName());
        return result.success(attend);
    }

    /**
     * 新增出勤
     * @param attend
     * @param principal
     * @return
     */
    @PostMapping("/sys/attend")
    public result attendSave(@RequestBody attend attend,Principal principal){
        User user = userService.getByUsername(principal.getName());
        attend.setDoctor_id(user.getId());
        long count = attendService.count(new QueryWrapper<attend>().eq("doctor_id", attend.getDoctor_id()).eq("date", attend.getDate()));
        if(count > 0){
            return result.fail("当天已预约过");
        }
        attendService.save(attend);
        return result.success("成功");
    }

    /**
     * 获取当天诊断表
     * @param principal
     * @return
     */
    @GetMapping("/sys/orders")
    public result getOrders(Principal principal){
        List<orderDoctorDto> orderDoctorDto = orderService.getDoctorOrders(principal,LocalDate.now());
        return result.success(orderDoctorDto);
    }

    /**
     * 获取诊断内容
     * @param id
     * @return
     */
    @GetMapping("/sys/orderInfo/{id}")
    public result getOrderInfo(@PathVariable int id){
        System.out.println(id);
        orderDoctorDto orderDoctorDto = orderService.getOrderById(id);
        return result.success(orderDoctorDto);
    }

    /**
     * 发出诊断
     * @param order
     * @return
     */
    @PostMapping("/sys/order")
    public result updateOrder(@RequestBody order order){
        System.out.println(order.getOrderId());
        order.setDiagnosisStatus(Const.STATUS_TRUE);
        orderService.updateById(order);
        redisUtil.hset(Const.PAIDUI,order.getAttendId().toString(),order.getNum()+1,new Times().getSecondsTobeforedawn());//到凌晨消失
        //如果是发热门诊出诊断，代表出院，将信息改回绿码
        if(Objects.equals(order.getAttendId(), Const.FARE_ORDER)){
            User user = userService.getById(order.getUserId());
            user.setCov(Const.STATUS_FALSE);
            user.setUpdated(LocalDateTime.now());
            userService.updateById(user);
        }
        return result.success("成功");
    }
}
