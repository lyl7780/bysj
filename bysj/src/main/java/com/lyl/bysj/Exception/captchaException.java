package com.lyl.bysj.Exception;


import org.springframework.security.core.AuthenticationException;

public class captchaException extends AuthenticationException {
    public captchaException(String msg) {
        super(msg);
    }
}
