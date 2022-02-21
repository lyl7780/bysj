package com.lyl.bysj.pojo;

import lombok.Data;
/*
* 预约表
* */
@Data
public class order {
    private Integer orderId;
    private Integer attendId;
    //报到状态
    private Boolean registerStatus;
    //诊断状态
    private Boolean diagnosisStatus;
    //诊断内容
    private String diagnosis;
}
