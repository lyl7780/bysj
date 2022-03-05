package com.lyl.bysj.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.lyl.bysj.controller.utils.Const;
import com.lyl.bysj.controller.utils.result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * 权限相关
 */
@RestController
public class AuthController extends BaseController{

    @Autowired
    Producer producer;


    /**
     * 获取验证码
     * @return
     * @throws IOException
     */
    @GetMapping("/captcha")
    public result captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        //测试用
        key = "aaaaa";
        code = "11111";

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"png",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/png;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtil.hset(Const.CAPTCHA_KEY,key,code,120);
        return result.success(MapUtil.builder().put("token",key).put("captchaImg",base64Img).build());
    }
}
