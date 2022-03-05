package com.lyl.bysj.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Role {
    private Integer id;
    @NotBlank(message = "角色名不能为空")
    private String name;
    @NotBlank(message = "角色编码不能为空")
    private String role;
    private String description;
    @NotNull(message = "状态不能为空")
    private Integer status;
    private LocalDateTime created;
    private LocalDateTime updated;
    @TableField(exist = false)
    private List<Integer> menuIds = new ArrayList<>();
}
