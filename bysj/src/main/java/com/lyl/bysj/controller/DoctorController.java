package com.lyl.bysj.controller;

import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.Doctor;
import com.lyl.bysj.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{username}/{password}")
    public result login(@PathVariable String username, @PathVariable String password, HttpServletRequest request){
        Doctor doctor = doctorService.login(username,password);
        boolean flag = (doctor != null);
        if(flag){
            //登录成功，保存数据
            HttpSession session = request.getSession();
            session.setAttribute("doctor",doctor.getDoctorId());
        }
        return new result(flag,doctor);
    }
}
