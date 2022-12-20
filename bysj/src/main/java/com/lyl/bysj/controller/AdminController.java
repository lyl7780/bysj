package com.lyl.bysj.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyl.bysj.common.dto.*;
import com.lyl.bysj.controller.utils.Const;
import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 后台专用controller
 * 普通管理员，超级管理员可以用
 * username: root
 * password: B055MAN33m.
 */
@PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController{

    @GetMapping("/test")
    public result test(){
        return result.success(123);
    }

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
     * 获取指定菜单信息（编辑中）
     * @param id
     * @return
     */
    @GetMapping("/sys/menu/info/{id}")
    public result menuInfo(@PathVariable int id){
        return result.success(menuService.getById(id));
    }

    /**
     * 删除指定菜单
     * @param id
     * @return
     */
    @DeleteMapping("/sys/menu/del/{id}")
    public result delMenu(@PathVariable int id){
        long count = menuService.count(new QueryWrapper<Menu>().eq("parent_id", id));
        if(count > 0){
            return result.fail("请先删除子菜单");
        }
        //清除所有与该菜单相关的权限缓存
        userService.clearUserAuthoritiesInfoByMenuId(id);
        //同步删除中间关联表
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id",id));
        menuService.removeById(id);
        return result.success("success");
    }

    /**
     * 更新指定菜单信息
     * @param menu
     * @return
     */
    @PostMapping("/sys/menu/update")
    public result updateMenu(@Validated @RequestBody Menu menu){
        menu.setUpdated(LocalDateTime.now());
        //清除所有与该菜单相关的权限缓存
        userService.clearUserAuthoritiesInfoByMenuId(menu.getId());
        menuService.updateById(menu);
        return result.success("success");
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @PostMapping("/sys/menu/save")
    public result saveMenu(@Validated @RequestBody Menu menu){
        menu.setCreated(LocalDateTime.now());
        return result.success(menuService.save(menu));
    }

    /**
     * 获取菜单列表
     * @return
     */
    @GetMapping("/sys/menu/list")
    public result menuList(){
        List<Menu> menus = menuService.tree();
        return result.success(menus);
    }

    /**
     * 根据ID获取角色（编辑中）
     * @param id
     * @return
     */
    @GetMapping("/sys/role/info/{id}")
    public result roleInfo(@PathVariable int id){
        Role role = roleService.getById(id);
        //获取角色相关联的菜单id
        List<RoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", id));
        List<Integer> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        role.setMenuIds(menuIds);
        return result.success(role);
    }

    /**
     * 获取角色信息(搜索）
     * @param name
     * @return
     */
    @GetMapping("/sys/role/list")
    public result roleList( String name){
        Page<Role> page = roleService.page(getPage(), new QueryWrapper<Role>().like(StrUtil.isNotBlank(name),"name",name));
        return result.success(page);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping("/sys/role/save")
    public result saveRole(@Validated @RequestBody Role role){
        Long count = roleService.getCountByRole(role.getRole());
        if(count > 0){
            return result.fail("该角色已存在");
        }
        role.setCreated(LocalDateTime.now());
        roleService.save(role);
        return result.success("成功");
    }

    /**
     * 更新角色
     * @param role
     * @return
     */
    @PostMapping("/sys/role/update")
    public result updateRole(@Validated @RequestBody Role role){
        Long count = roleService.getCountByRole(role.getRole());
        if(count > 0){
            role.setUpdated(LocalDateTime.now());
            roleService.updateById(role);
            //清除相应缓存
            userService.clearUserAuthoritiesInfoByRoleId(role.getId());
            return result.success("更新成功");
        }
        return result.fail("该角色不存在");
    }

    /**
     * 批量删除角色
     * @param ids
     * @return
     */
    @PostMapping("/sys/role/del")
    @Transactional
    public result delRole(@RequestBody Integer[] ids){
        roleService.removeByIds(Arrays.asList(ids));
        //删除中间表
        userRoleService.remove(new QueryWrapper<UserRole>().in("role_id",ids));
        roleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id",ids));
        //清除相应缓存
        Arrays.stream(ids).forEach(id ->{
            userService.clearUserAuthoritiesInfoByRoleId(id);
        });
        return result.success("成功");
    }

    /**
     * 更新角色可控菜单
     * @param roleId
     * @param menuIds
     * @return
     */
    @PutMapping("/sys/role/perm/{id}")
    @Transactional
    public result roleMenu(@PathVariable("id") int roleId,@RequestBody int[] menuIds){
        List<RoleMenu> roleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId ->{
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenus.add(roleMenu);
        });
        //先删除原来的记录，再添加
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        roleMenuService.saveBatch(roleMenus);
        //清除相关缓存
        userService.clearUserAuthoritiesInfoByRoleId(roleId);
        return result.success(menuIds);
    }

    /**
     * 获取用户列表
     * @param name
     * @return
     */
    @GetMapping("/sys/user/list")
    public result userList(String name){
        Page<User> pages = userService.userListPage(getPage(),name, Const.ROLE_USER);
        return result.success(pages);
    }

    /**
     * 查询用户信息（编辑）
     * @param id
     * @return
     */
    @GetMapping("/sys/user/info/{id}")
    public result getUserInfo(@PathVariable int id){
        UserDto user = userService.getByIdToDto(id);
        Assert.notNull(user,"找不到该用户");
        return result.success(user);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PutMapping("/sys/user")
    public result editUserInfo(@RequestBody User user){
        user.setUpdated(LocalDateTime.now());
        userService.updateById(user);
        //清除相应缓存
        userService.clearUserAuthoritiesInfo(user.getId());
        return result.success("成功");
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @PostMapping("/sys/user/del")
    public result delUser(@RequestBody Integer[] ids){
        userService.removeByIds(Arrays.asList(ids));
        //删除中间表
        userRoleService.remove(new QueryWrapper<UserRole>().in("user_id",ids));
        //清除相应缓存
        Arrays.stream(ids).forEach(id ->{
            userService.clearUserAuthoritiesInfo(id);
        });
        return result.success("成功");
    }

    /**
     * 重置用户密码:888888
     * @param id
     * @return
     */
    @PostMapping("/sys/user/repass")
    public result repassUser(@RequestBody int id){
        User user = userService.getById(id);
        if(user == null){
            return result.fail("该用户不存在");
        }
        //清除相应缓存
        userService.clearUserAuthoritiesInfo(id);
        user.setPassword(passwordEncoder.encode("888888"));
        userService.updateById(user);
        return result.success("成功");
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
     * 获取医生信息
     * @param name
     * @return
     */
    @GetMapping("/sys/doctor/list")
    public result doctorList(String name){
        Page<DoctorDto> pages = userService.doctorListPage(getPage(),name);
        return result.success(pages);
    }

    /**
     * 删除指定医生
     * @param ids
     * @return
     */
    @PostMapping("/sys/doctor/del")
    public result delDoctor(@RequestBody Integer[] ids){
        userService.removeByIds(Arrays.asList(ids));
        //删除中间表
        userRoleService.remove(new QueryWrapper<UserRole>().in("user_id",ids));
        departmentDoctorService.remove(new QueryWrapper<departmentDoctor>().in("doctor_id",ids));
        //清除相应缓存
        Arrays.stream(ids).forEach(id ->{
            userService.clearUserAuthoritiesInfo(id);
        });
        return result.success("成功");
    }

    /**
     * 医生注册
     * @param dto
     * @return
     */
    @PostMapping("/sys/doctor/save")
    public result saveDoctor(@RequestBody DoctorFormDto dto){
        long count = departmentService.count(new QueryWrapper<department>().eq("office_id",dto.getOfficeId()));
        if(count <= 0){
            return result.fail("该部门不存在");
        }
        long count1 = userService.count(new QueryWrapper<User>().eq("username", dto.getUsername()));
        if(count1 > 0){
            return result.fail("该用户已注册");
        }
        userService.saveDoctor(dto);
        UserRole userRole = new UserRole();
        userRole.setUserId(dto.getId());
        userRole.setRoleId(Const.ROLE_DOCTOR);
        userRoleService.save(userRole);
        departmentDoctor d = new departmentDoctor();
        d.setDoctor_id(dto.getId());
        d.setDepartment(dto.getOfficeId());
        departmentDoctorService.save(d);
        return result.success("成功");
    }

    /**
     * 修改医生信息
     * @param dto
     * @return
     */
    @PostMapping("/sys/doctor/update")
    public result updateDoctor(@RequestBody DoctorFormDto dto){
        long count = departmentService.count(new QueryWrapper<department>().eq("office_id",dto.getOfficeId()));
        if(count <= 0){
            return result.fail("该部门不存在");
        }
        long count1 = userService.count(new QueryWrapper<User>().eq("username", dto.getUsername()));
        if(count1 <= 0){
            return result.fail("禁止更改用户名");
        }
        userService.clearUserAuthoritiesInfo(dto.getId());
        userService.updateDoctor(dto);
        departmentDoctor departmentDoctor = departmentDoctorService.getOne(new QueryWrapper<departmentDoctor>().eq("doctor_id", dto.getId()));
        departmentDoctor.setDepartment(dto.getOfficeId());
        departmentDoctorService.updateById(departmentDoctor);
        return result.success("成功");
    }

    /**
     * 获取指定医生信息（编辑中）
     * @param id
     * @return
     */
    @GetMapping("/sys/doctor/info/{id}")
    public result doctorInfo(@PathVariable int id){
        DoctorFormDto dto = userService.doctorInfo(id);
        Assert.notNull(dto,"找不到该用户");
        return result.success(dto);
    }

    /**
     * 新增部门
     * @param department
     * @return
     */
    @PostMapping("/sys/department/save")
    public result saveDepartment(@RequestBody department department){
        long num = departmentService.count(new QueryWrapper<department>().eq("dname", department.getName()));
        if(num != 0){
            return result.fail("该部门已存在");
        }
        departmentService.save(department);
        return result.success("成功");
    }

    /**
     * 修改部门
     * @param department
     * @return
     */
    @PostMapping("/sys/department/update")
    public result updateDepartment(@RequestBody department department){
        long count = departmentService.count(new QueryWrapper<department>().eq("office_id", department.getOfficeId()));
        if(count == 0){
            return result.fail("该部门不存在");
        }
        departmentService.updateById(department);
        return result.success("成功");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @DeleteMapping("/sys/department/del/{id}")
    public result delDepartment(@PathVariable int id){
        long count = departmentService.count(new QueryWrapper<department>().eq("office_id", id));
        if(count == 0){
            System.out.println(id);
            return result.fail("该部门不存在");
        }
        long count1 = departmentDoctorService.count(new QueryWrapper<departmentDoctor>().eq("department", id));
        if(count1 != 0){
            return result.fail("您必须先删除该部门下的医生");
        }
        departmentService.removeById(id);
        return result.success("成功");
    }

    /**
     * 预计接诊人数
     * @return
     */
    @GetMapping("/sys/orderToday")
    public result orderToday(){
        int orders = orderService.countOrderToday();
        return result.success(orders);
    }

    /**
     * 疑似人数
     * @return
     */
    @GetMapping("/sys/covCount")
    public result covCount(){
        long users = userService.count(new QueryWrapper<User>().eq("cov",1));
        return result.success(users);
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
     * 管理员改密码(爷改密码)
     * @param passDto
     * @param principal
     * @return
     */
    @PostMapping("/resetPassword")
    public result resetPassword(@Validated @RequestBody PassDto passDto,Principal principal){
        return rstPassword(passDto,principal);
    }

}
