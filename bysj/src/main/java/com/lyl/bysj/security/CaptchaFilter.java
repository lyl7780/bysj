package com.lyl.bysj.security;

import com.lyl.bysj.Exception.captchaException;
import com.lyl.bysj.controller.utils.Const;
import com.lyl.bysj.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        if (("/login".equals(url) || "/admin/login".equals(url) || "/doctor/login".equals(url)) && httpServletRequest.getMethod().equals("POST")) {
            try {
                //校验验证码
                validate(httpServletRequest);
            } catch (captchaException e) {
                //交给失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                e.printStackTrace();
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    //校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("token");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new captchaException("验证码不能为空");
        }
        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            throw new captchaException("验证码错误");
        }

        //一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}
