package com.lyl.bysj.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.common.dto.MenuDto;
import com.lyl.bysj.dao.MenuDao;
import com.lyl.bysj.dao.UserDao;
import com.lyl.bysj.pojo.Menu;
import com.lyl.bysj.pojo.User;
import com.lyl.bysj.service.MenuService;
import com.lyl.bysj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Override
    public List<MenuDto> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        List<Integer> menuIds = userDao.getNavMenuIds(user.getId());
        List<Menu> menus = this.listByIds(menuIds);
        //转树状结构
        List<Menu> menuTree = buildTreeMenu(menus);
        //实体转DTO
        return convert(menuTree);
    }

    @Override
    public List<Menu> tree() {
        //获取所有菜单信息
        List<Menu> menus = this.list(new QueryWrapper<Menu>().orderByAsc("order_num"));
        //转成树状结构
        return buildTreeMenu(menus);
    }

    private List<MenuDto> convert(List<Menu> menuTree) {
        List<MenuDto> menuDtos = new ArrayList<>();
        menuTree.forEach(m ->{
            MenuDto dto = new MenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerm());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            dto.setIcon(m.getIcon());
            dto.setCreated(m.getCreated());
            dto.setUpdated(m.getUpdated());
            if(m.getChildren().size() > 0){
                //子节点递归转换
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }

    private List<Menu> buildTreeMenu(List<Menu> menus) {
        List<Menu> finalMenus = new ArrayList<>();
        //各自寻找到各自的孩子
        for(Menu menu : menus){
            for(Menu e: menus){
                if(Objects.equals(menu.getId(), e.getParentId())){
                    menu.getChildren().add(e);
                }
            }
            //提取父节点
            if(menu.getParentId() == 0){
                finalMenus.add(menu);
            }
        }
        System.out.println(JSONUtil.toJsonStr(finalMenus));
        //提取出父节点

        return finalMenus;
    }


}
