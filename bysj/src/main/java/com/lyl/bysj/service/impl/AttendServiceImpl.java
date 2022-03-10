package com.lyl.bysj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyl.bysj.common.dto.AttendDto;
import com.lyl.bysj.dao.attendDao;
import com.lyl.bysj.pojo.attend;
import com.lyl.bysj.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendServiceImpl extends ServiceImpl<attendDao, attend> implements AttendService {

    @Autowired
    private attendDao attendDao;

    @Override
    public Page<AttendDto> getAttends(Page page, LocalDate date,String username) {
        List<AttendDto> attends = attendDao.getAttends(page.getCurrent()-1,page.getSize(),date,username);
        page.setTotal(attends.size());
        page.setRecords(attends);
        return page;
    }

    @Override
    public Page<AttendDto> userGetAttends(Page page, LocalDate date, int officeId, String username) {
        List<AttendDto> attends = attendDao.userGetAttends(page.getCurrent()-1,page.getSize(),date,officeId,username);
        page.setTotal(attends.size());
        page.setRecords(attends);
        return page;
    }
}
