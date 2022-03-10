package com.lyl.bysj.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/*
* 科室
* */
@Data
public class department {
    @TableId
    private Integer officeId;
    //科室名
    @TableField("dname")
    private String name;
}
