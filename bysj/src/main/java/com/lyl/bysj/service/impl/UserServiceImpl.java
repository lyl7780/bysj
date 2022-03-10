package com.lyl.bysj.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.common.dto.DoctorDto;
import com.lyl.bysj.common.dto.DoctorFormDto;
import com.lyl.bysj.common.dto.UserDto;
import com.lyl.bysj.common.vo.UserVo;
import com.lyl.bysj.controller.utils.Const;
import com.lyl.bysj.dao.UserDao;
import com.lyl.bysj.pojo.Menu;
import com.lyl.bysj.pojo.Role;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.service.MenuService;
import com.lyl.bysj.service.RoleService;
import com.lyl.bysj.service.UserService;
import com.lyl.bysj.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.eq(User::getUsername,username);
        return userDao.selectOne(lqw);
    }

    @Override
    public String getUserAuthoritiesInfo(int id) {

        User user = userDao.selectById(id);
        String authorities = "";
        //查询是否有缓存
        if(redisUtil.hasKey("GrantedAuthorities:"+user.getId())){
            authorities = (String) redisUtil.get("GrantedAuthorities:"+user.getId());
        }else{
            //查询角色编码
            List<Role> roles = roleService.list(new QueryWrapper<Role>().inSql("id","select role_id from user_role where user_id="+id));
            if(roles.size() > 0){
                String roleCodes = roles.stream().map(Role::getRole).collect(Collectors.joining(","));
                authorities = roleCodes;
            }
            //查询菜单操作编码
            List<Integer> menuIds = userDao.getNavMenuIds(id);
            if(menuIds.size() > 0){
                List<Menu> menus = menuService.listByIds(menuIds);
                String menuPerms = menus.stream().map(Menu::getPerm).collect(Collectors.joining(","));
                if(!"".equals(authorities)){
                    authorities = authorities.concat(",");
                }
                authorities = authorities.concat(menuPerms);
            }
            //存入redis缓存
            redisUtil.set("GrantedAuthorities:"+user.getId(),authorities,60*60);
        }
        return authorities;
    }

    @Override
    public void clearUserAuthoritiesInfo(int userId) {
        redisUtil.del("GrantedAuthorities:"+userId);
    }

    @Override
    public void clearUserAuthoritiesInfoByRoleId(int roleId) {
        List<User> users = this.list(new QueryWrapper<User>().inSql("id", "select user_id from user_role where role_id=" + roleId));
        users.forEach(u ->{
            this.clearUserAuthoritiesInfo(u.getId());
        });
    }

    @Override
    public void clearUserAuthoritiesInfoByMenuId(int menuId) {
        List<User> users = userDao.listByMenuId(menuId);
        users.forEach(u ->{
            this.clearUserAuthoritiesInfo(u.getId());
        });
    }

    @Override
    public UserDto getByIdToDto(int id) {
        User user = this.getById(id);
        return convert(user);
    }

    @Override
    public Page<User> userListPage(Page page, String username,int type) {
        Page<User> pages = this.page(page, new QueryWrapper<User>().like(StrUtil.isNotBlank(username), "username", username)
                        .inSql("id","select user_id from user_role where role_id="+ type)
                .select("id","avatar","username","name","id_card","phone","description","status","cov","created","updated"))
                ;
        return pages;
    }

    /**
     * 查找医生列表（用MP写了一整天后，我妥协了，就用这个原始方法吧）
     * @param page
     * @param name
     * @return
     */
    @Override
    public Page<DoctorDto> doctorListPage(Page page,String name){
        List<DoctorDto> doctors = userDao.doctorList(page.getCurrent()-1,page.getSize(),name);
        page.setTotal(doctors.size());
        page.setRecords(doctors);
        return page;
    }

    @Override
    public DoctorFormDto doctorInfo(int id) {
        DoctorFormDto dto = userDao.doctorInfo(id);
        return dto;
    }

    @Override
    public void updateDoctor(DoctorFormDto dto) {
        User user = ConvertToUser(dto);
        this.updateById(user);
    }

    @Override
    public void saveDoctor(DoctorFormDto dto) {
        User user = ConvertToUser(dto);
        user.setAvatar("https://a.ideaopen.cn/laoluwoye/uhVaNsHN.png");
        user.setPassword(passwordEncoder.encode("888888"));
        user.setCreated(LocalDateTime.now());
        this.save(user);
        dto.setId(user.getId());
    }

    @Override
    public void saveUser(UserVo userVo) {
        User user = VoConvert(userVo);
        this.save(user);
        userVo.setId(user.getId());
    }

    public User VoConvert(UserVo userVo){
        User user = new User();
        user.setId(userVo.getId());
        user.setAvatar(userVo.getAvatar());
        user.setUsername(userVo.getUsername());
        user.setName(userVo.getName());
        user.setPassword(userVo.getPassword());
        user.setIdCard(userVo.getIdCard());
        user.setPhone(userVo.getPhone());
        user.setCreated(userVo.getCreated());
        return user;
    }

    public User ConvertToUser(DoctorFormDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setAvatar(dto.getAvatar());
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setDescription(dto.getDescription());
        user.setStatus(dto.getStatus());
        user.setCreated(dto.getCreated());
        user.setUpdated(dto.getUpdated());
        return user;
    }

    public UserDto convert(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setAvatar(user.getAvatar());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setIdCard(user.getIdCard());
        userDto.setPhone(user.getPhone());
        userDto.setDescription(user.getDescription());
        userDto.setStatus(user.getStatus());
        userDto.setCov(user.getCov());
        userDto.setCreated(user.getCreated());
        userDto.setUpdated(user.getUpdated());
        return userDto;
    }

}
