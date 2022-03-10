package com.lyl.bysj.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyl.bysj.common.dto.MenuDto;
import com.lyl.bysj.common.dto.PassDto;
import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.pojo.department;
import com.lyl.bysj.service.*;
import com.lyl.bysj.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

public class BaseController {
    @Autowired
    protected HttpServletRequest req;
    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    protected UserService userService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected RoleMenuService roleMenuService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected AttendService attendService;
    @Autowired
    protected UserRoleService userRoleService;
    @Autowired
    protected DepartmentService departmentService;
    @Autowired
    protected DepartmentDoctorService departmentDoctorService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    OrderService orderService;

    /**
     * 获取页码
     * @return
     */
    public Page getPage(){
        int current = ServletRequestUtils.getIntParameter(req,"current",1);
        int size = ServletRequestUtils.getIntParameter(req,"size",10);
        return new Page(current,size);
    }

    /**
     * 权限获取
     * @param principal
     * @return
     */
    public result getNav(Principal principal){
        User user = userService.getByUsername(principal.getName());
        //获取权限信息
        String authoritiesInfo = userService.getUserAuthoritiesInfo(user.getId());
        String[] authoritiesInfoArray = StringUtils.tokenizeToStringArray(authoritiesInfo, ",");
        //获取导航栏信息
        List<MenuDto> navs = menuService.getCurrentUserNav();
        return result.success(MapUtil.builder().put("authorities",authoritiesInfoArray).put("nav",navs).map());
    }

    /**
     * 修改密码
     * @param passDto
     * @param principal
     * @return
     */
    public result rstPassword(PassDto passDto, Principal principal){
        User user = userService.getByUsername(principal.getName());

        boolean matches = passwordEncoder.matches(passDto.getOldPassword(), user.getPassword());
        if(!matches){
            return result.fail("密码不正确");
        }
        boolean matches2 = passwordEncoder.matches(passDto.getOldPassword(), passDto.getNewPassword());
        if(matches2){
            return result.fail("新旧密码不能一致");
        }
        user.setPassword(passwordEncoder.encode(passDto.getNewPassword()));
        userService.updateById(user);
        return result.success("成功");
    }


    public result getDepartments(){
        List<department> departments = departmentService.list();
        System.out.println(departments);
        return result.success(departments);
    }
}
