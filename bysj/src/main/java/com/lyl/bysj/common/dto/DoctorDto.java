package com.lyl.bysj.common.dto;

import com.lyl.bysj.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorDto extends BaseEntity {
    private String avatar;
    private String username;
    private String name;
    private String office;
    private String description;
}
