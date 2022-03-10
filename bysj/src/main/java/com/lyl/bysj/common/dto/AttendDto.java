package com.lyl.bysj.common.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendDto {
    private Integer attendId;
    private String name;
    private String description;
    @TableField("dname")
    private String department;
    private LocalDate date;
    private Integer number;
}
