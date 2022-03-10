package com.lyl.bysj.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/*
* 预约表
* */
@Data
@TableName("`order`")
public class order {
    @TableId
    private Integer orderId;
    private Integer userId;
    private Integer attendId;
    //报到状态
    private Integer registerStatus;
    //诊断状态
    private Integer diagnosisStatus;
    //诊断内容
    private String diagnosis;
}
