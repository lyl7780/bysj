package com.lyl.bysj.controller.utils;

import lombok.Data;
/*
* 前后端数据协议
* */
@Data
public class result {
    private int code;
    private Object data;
    private String msg;

    public result(){}

    public static result success(Object o){
        return success(200,"操作成功",o);
    }

    public static result fail(String msg){
        return fail(400,msg,null);
    }

    public static result success(int code,String msg,Object data){
        result r = new result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static result fail(int code,String msg,Object data){
        result r = new result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
