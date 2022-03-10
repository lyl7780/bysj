package com.lyl.bysj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 身份证号判别
 */
public class idParse {
    /**
     * 判断年龄
     * @param IdNO
     * @return
     */
    public static int age(String IdNO) {
        String dates = "";
        dates = IdNO.substring(6, 10);
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(new Date());
        return Integer.parseInt(year) - Integer.parseInt(dates);
    }

    /**
     * 判断男女
     * 0：女
     * 1：男
     * @param IdNO
     * @return
     */
    public static int sex(String IdNO) {
        String sexNo = "";
        sexNo = IdNO.substring(16, 17);
        return Integer.parseInt(sexNo) % 2 == 0 ? 0 : 1;
    }
}
