package com.lyl.bysj.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.lyl.bysj.Exception.captchaException;
import com.lyl.bysj.common.vo.UserVo;
import com.lyl.bysj.controller.utils.Const;
import com.lyl.bysj.controller.utils.abaaba;
import com.lyl.bysj.controller.utils.result;
import com.lyl.bysj.pojo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;


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
//        key = "aaaaa";
//        code = "11111";

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"png",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/png;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtil.hset(Const.CAPTCHA_KEY,key,code,120);
        return result.success(MapUtil.builder().put("token",key).put("captchaImg",base64Img).build());
    }

    /**
     * 用户注册
     * @param userVo
     * @return
     */
    @PostMapping("/register")
    public result userRegister(@Validated @RequestBody UserVo userVo){
        String code = userVo.getCode();
        String key = userVo.getToken();
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            //一次性使用
            redisUtil.hdel(Const.CAPTCHA_KEY,key);
            return result.fail("验证码不能为空");
        }
        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            //一次性使用
            redisUtil.hdel(Const.CAPTCHA_KEY,key);
            return result.fail("验证码错误");
        }
        //一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY,key);
        if(!userVo.getPassword().equals(userVo.getRepassword())){
            return result.fail("两次输入的密码不相同");
        }
        if(userVo.getIdCard().length() != 18){
            return result.fail("身份证必须为18位");
        }
        if(userVo.getPhone().length() != 11){
            return result.fail("手机号必须为11位");
        }
        if(userService.getByUsername(userVo.getUsername())!=null){
            return result.fail("该用户名已被注册");
        }
        userVo.setAvatar("https://a.ideaopen.cn/laoluwoye/uhVaNsHN.png");
        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        userVo.setCreated(LocalDateTime.now());
        userService.saveUser(userVo);
        UserRole u = new UserRole();
        u.setUserId(userVo.getId());
        u.setRoleId(Const.ROLE_USER);
        userRoleService.save(u);
        return result.success("成功");
    }

    @GetMapping("/lll/test")
    public result test(){
        abaaba abaaba = new abaaba();
        abaaba.setToken("121242r");
        return new result(304,abaaba,"redirect:/login");
    }
}
