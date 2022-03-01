package com.lyl.bysj.pojo;

import lombok.Data;
/*
* 用户
* */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String idCard;
    private String phone;
    private String description;
    private Boolean status;
    //是否为新冠疑似患者
    private Boolean cov;
}
