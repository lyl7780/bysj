package com.lyl.bysj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lyl.bysj.dao")
public class BysjApplication {

    public static void main(String[] args) {
        SpringApplication.run(BysjApplication.class, args);
    }

}
