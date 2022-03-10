package com.lyl.bysj.common.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserVo {
    private int id;
    private String avatar;
    @NotBlank(message = "请输入用户名")
    private String username;
    @NotBlank(message = "请输入密码")
    private String password;
    @NotBlank(message = "请再输入一遍密码")
    private String repassword;
    @NotBlank(message = "请输入姓名")
    private String name;
    @NotBlank(message = "请输入身份证号")
    private String idCard;
    @NotBlank(message = "请输入手机号")
    private String phone;
    @NotBlank(message = "请输入验证码")
    private String code;
    private String token;
    private LocalDateTime created;
}
