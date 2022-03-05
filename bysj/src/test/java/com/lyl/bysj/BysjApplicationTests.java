package com.lyl.bysj;

import com.lyl.bysj.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BysjApplicationTests {

    @Autowired
    public UserDao userDao;



}
