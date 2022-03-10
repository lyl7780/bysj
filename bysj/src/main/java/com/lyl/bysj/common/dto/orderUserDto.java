package com.lyl.bysj.common.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class orderUserDto {
    private Integer orderId;
    private String name;
    private String department;
    private String description;
    private LocalDate date;
    private Integer registerStatus;
}
