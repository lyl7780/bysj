package com.lyl.bysj.pojo;

import lombok.Data;
/*
* 单项预约
* */
@Data
public class attendDoctor {
    //医生信息
    private Integer doctorId;
    private String doctorName;
    private String description;
    //剩余可预约数
    private Integer number;
}
