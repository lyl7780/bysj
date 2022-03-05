package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.common.dto.MenuDto;
import com.lyl.bysj.pojo.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
 List<MenuDto> getCurrentUserNav();

    List<Menu> tree();
}
