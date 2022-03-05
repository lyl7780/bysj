package com.lyl.bysj.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
/*
* 科室
* */
@Data
public class department {
    private Integer officeId;
    //科室名
    @TableField("dname")
    private String name;
}
