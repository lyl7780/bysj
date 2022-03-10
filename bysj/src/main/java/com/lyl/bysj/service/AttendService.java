package com.lyl.bysj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyl.bysj.common.dto.AttendDto;
import com.lyl.bysj.pojo.attend;

import java.time.LocalDate;

public interface AttendService extends IService<attend> {
    Page<AttendDto> getAttends(Page page, LocalDate date,String username);

    Page<AttendDto> userGetAttends(Page page, LocalDate date, int officeId, String username);
}
