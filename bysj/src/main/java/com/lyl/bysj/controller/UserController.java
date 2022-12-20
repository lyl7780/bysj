package com.lyl.bysj.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyl.bysj.common.dto.*;
import com.lyl.bysj.controller.utils.Const;
import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.pojo.attend;
import com.lyl.bysj.pojo.order;
import com.lyl.bysj.service.UserService;
import com.lyl.bysj.utils.LocalDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{
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
     * 获取部门信息
     * @return
     */
    @GetMapping("/sys/doctor/departments")
    public result getDepartments(){
        return super.getDepartments();
    }

    /**
     * 获取出诊信息
     * @param date
     * @param officeId
     * @return
     */
    @GetMapping("/sys/doctor/attend")
    public result getAttends(String date,int officeId,Principal principal){
        Page<AttendDto> attend = attendService.userGetAttends(getPage(),new LocalDateConverter("yyyy-MM-dd").convert(date),officeId,principal.getName());
        return result.success(attend);
    }

    /**
     * 进行预约
     * @param id
     * @param principal
     * @return
     */
    @PostMapping("/sys/order/{id}")
    public result doOrder(@PathVariable Integer id,Principal principal){
        User user = userService.getByUsername(principal.getName());
        if(user.getCov() != 0){
            return result.fail("您无法进行预约");
        }
        long count = orderService.count(new QueryWrapper<order>().eq("user_id", user.getId()).eq("attend_id", id));
        if(count != 0){
            return result.fail("您已经预约过了");
        }
        order order = new order();
        order.setUserId(user.getId());
        order.setAttendId(id);
        order.setRegisterStatus(0);
        order.setDiagnosisStatus(0);
        orderService.save(order);
        //可预约数-1
        attend attend = attendService.getById(id);
        attend.setNumber(attend.getNumber()-1);
        attendService.updateById(attend);
        return result.success("成功");
    }

    /**
     * 我的预约
     * @param principal
     * @return
     */
    @GetMapping("/sys/order/my")
    public result getOrder(String date, Principal principal){
        User user = userService.getByUsername(principal.getName());
        Page<orderUserDto> orderUserDto = orderService.getOrders(getPage(),user.getId(),new LocalDateConverter("yyyy-MM-dd").convert(date));
        return result.success(orderUserDto);
    }

    /**
     * 签到
     * @param id
     * @return
     */
    @PostMapping("/sys/doRegister/{id}/{cov}")
    public result doRegister(@PathVariable int id,@PathVariable int cov){
        order order = orderService.getById(id);
        User user = userService.getById(order.getUserId());
        //设置感染状态
        user.setCov(cov);
        userService.updateById(user);
        //设置签到状态
        order.setRegisterStatus(Const.REGISTERSTATUS_ON);
        //查询当前排队人数
        long num = orderService.count(new QueryWrapper<order>().isNotNull("num").eq("attend_id",id));
        //设置排队号
        order.setNum((int)num+1);
        orderService.updateById(order);
        return result.success("成功");
    }

    /**
     * 查看排队状态
     * @param orderId
     * @return
     */
    @GetMapping("/sys/register/paidui/{orderId}")
    public result checkPaidui(@PathVariable int orderId){
        order order = orderService.getById(orderId);
        PaiduiDto paiduiDto = new PaiduiDto();
        paiduiDto.setMe(order.getNum());
        paiduiDto.setNow((Integer) (redisUtil.hget(Const.PAIDUI, order.getAttendId().toString()) == null ?1:redisUtil.hget(Const.PAIDUI,order.getAttendId().toString())));
        return result.success(paiduiDto);
    }

    /**
     * 查询我的诊断
     * @param date
     * @param principal
     * @return
     */
    @GetMapping("/sys/mySis")
    public result mySis(String date,Principal principal){
        User user = userService.getByUsername(principal.getName());
        Page<orderUserDto> orderUserDto = orderService.getSis(getPage(),user.getId(),new LocalDateConverter("yyyy-MM-dd").convert(date));
        return result.success(orderUserDto);
    }

    /**
     * 查看诊断内容
     * @param id
     * @return
     */
    @GetMapping("/sys/sisInfo/{id}")
    public result sisInfo(@PathVariable int id){
        orderDoctorDto order = orderService.getOrderById(id);
        return result.success(order);
    }

    /**
     * 预约发热应急处理
     * @param principal
     * @return
     */
    @PostMapping("/sys/emergencyOrder/{cov}")
    public result emergencyOrder(Principal principal,@PathVariable int cov){
        User user = userService.getByUsername(principal.getName());
        //更新cov信息
        user.setCov(cov);
        user.setUpdated(LocalDateTime.now());
        userService.updateById(user);
        //删除今天以后所有的预约
        orderService.removeAllNewOrder(user.getId());
        //新建发热门诊的预约
        order order = new order();
        order.setUserId(user.getId());
        order.setAttendId(Const.FARE_ORDER);
        orderService.save(order);
        return result.success("转移成功");
    }

    /**
     * 现场发热应急处理
     * @return principal
     */
    @PostMapping("/sys/emergencyCheck/{cov}")
    public result emergencyCheck(Principal principal,@PathVariable int cov){
        User user = userService.getByUsername(principal.getName());
        //更新cov信息
        user.setCov(cov);
        user.setUpdated(LocalDateTime.now());
        userService.updateById(user);
        //删除今天以后的所有预约
        orderService.removeAllNewOrder(user.getId());
        //新建发热门诊的预约
        order order = new order();
        order.setUserId(user.getId());
        order.setRegisterStatus(Const.STATUS_TRUE);
        order.setAttendId(Const.FARE_ORDER);
        orderService.save(order);
        return result.success("转移成功");
    }

}
