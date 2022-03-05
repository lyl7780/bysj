package com.lyl.bysj.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
public class Menu extends BaseEntity{
    @NotNull(message = "上级菜单不能为空")
    private Integer parentId;
    @NotBlank(message = "菜单名不能为空")
    private String name;
    private String path;
    @NotBlank(message = "权限编码不能为空")
    @TableField("perms")
    private String perm;
    private String component;
    @NotNull(message = "类型不能为空")
    private Integer type;
    @NotBlank(message = "图标不能为空")
    private String icon;
    private String orderNum;
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();
}
