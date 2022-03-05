package com.lyl.bysj.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserDto implements Serializable {
    private Integer id;
    private String avatar;
    private String username;
    private String name;
    private String idCard;
    private String phone;
    private String description;
    private Integer status;
    //是否为新冠疑似患者
    private Integer cov;
    private LocalDateTime created;
    private LocalDateTime updated;
}
