package com.lyl.bysj.pojo;

import lombok.Data;
/*
* 用户
* */
@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String name;
    private String idCard;
    private String phone;
    //是否为新冠疑似患者
    private Boolean status;
}
