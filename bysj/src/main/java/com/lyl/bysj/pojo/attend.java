package com.lyl.bysj.pojo;

import lombok.Data;
import java.util.Date;
/*
* 出勤
* */
@Data
public class attend {
    private Integer attend_id;
    private Integer doctor_id;
    private String date;
    //当日剩余可预约数
    private Integer number;
}
