package com.lyl.bysj.controller.utils;

import lombok.Data;
/*
* 前后端数据协议
* */
@Data
public class result {
    private boolean flag;
    private Object data;

    public result(){}

    public result(boolean flag){
        this.flag = flag;
    }

    public result(boolean flag,Object data){
        this.flag = flag;
        this.data = data;
    }
}
