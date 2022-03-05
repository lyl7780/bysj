package com.lyl.bysj.common.dto;

import com.lyl.bysj.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorFormDto extends BaseEntity {
    private String avatar;
    private String username;
    private String password;
    private String name;
    private int officeId;
    private String description;
}
