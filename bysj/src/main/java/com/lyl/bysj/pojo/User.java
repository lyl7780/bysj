package com.lyl.bysj.pojo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/*
* 用户
* */
@Data
public class User {
    private Integer id;
    private String avatar;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String name;
    private String idCard;
    private String phone;
    private String description;
    private Integer status;
    //是否为新冠疑似患者
    private Integer cov;
    private LocalDateTime created;
    private LocalDateTime updated;
}
