package com.lyl.bysj.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class orderDoctorDto implements Serializable {
    private Integer orderId;
    private String name;
    private String idCard;
    private Integer sex;
    private Integer age;
    private LocalDate date;
    private Integer cov;
    private Integer diagnosisStatus;
    private String diagnosis;
}
