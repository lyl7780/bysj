package com.lyl.bysj.pojo;

import lombok.Data;
/*
* 医生
* */
@Data
public class Doctor {
    private Integer doctorId;
    private String username;
    private String password;
    private String name;
    private Integer officeId;
    private String description;
}
