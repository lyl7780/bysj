package com.lyl.bysj.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
/*
* 出勤
* */
@Data
public class attend {
    @TableId
    private Integer attend_id;
    @NotNull(message = "请输入当前用户")
    private Integer doctor_id;
    @NotBlank(message = "请输入日期")
    private String date;
    //当日剩余可预约数
    @NotNull(message = "请输入接诊数")
    private Integer number;
}
