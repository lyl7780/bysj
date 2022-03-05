package com.lyl.bysj.security;

import com.lyl.bysj.pojo.User;
import com.lyl.bysj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    /**
     * 用户验证
     * @param s  用户名
     * @return   返回用户信息及权限信息
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getByUsername(s);
        if(user == null){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(user.getId(),user.getUsername(),user.getPassword(),getUserAuthorities(user.getId()));
    }

    /**
     * 获取用户权限信息(角色，菜单（如果有的话）)
     * @param id
     * @return
     */
    public List<GrantedAuthority> getUserAuthorities(int id){

        //角色(ROLE_ADMIN)，菜单操作权限(sys:user:list)
        String authorities = userService.getUserAuthoritiesInfo(id);   //ROLE_ADMIN,sys:user:list,.....
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }
}
