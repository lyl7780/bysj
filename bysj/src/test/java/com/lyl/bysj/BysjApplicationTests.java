package com.lyl.bysj;

import com.lyl.bysj.dao.UserDao;
import com.lyl.bysj.pojo.Doctor;
import com.lyl.bysj.pojo.attendDoctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BysjApplicationTests {

    @Autowired
    public UserDao userDao;

    @Test
    void contextLoads() {
//        userDao.selectList(null);
        List<attendDoctor> attendDoctors = userDao.selectByDateAndOffice("2022-02-20",0);
        System.out.println(attendDoctors);
    }

}
