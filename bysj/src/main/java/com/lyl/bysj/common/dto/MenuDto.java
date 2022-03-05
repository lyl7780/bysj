package com.lyl.bysj.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *                     name: 'sysUser',
 *                     title: '用户管理',
 *                     icon: 'el-icon-s-custom',
 *                     component: 'admin/sys/User',
 *                     path: '/admin/sys/user',
 *                     children: []
 */
@Data
public class MenuDto implements Serializable {
    private Integer id;
    private String name;
    private String title;
    private String icon;
    private String component;
    private Integer type;
    private String path;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<MenuDto> children = new ArrayList<>();
}
