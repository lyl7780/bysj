package com.lyl.bysj.config;

import com.lyl.bysj.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //这里的接口不拦截
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/register",
            "/lll/test",
            "favicon.ico"
    };

    protected void configure(HttpSecurity http) throws Exception {


        http.cors().and().csrf().disable()

        //登录配置
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)

                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
        //禁用Session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().headers().frameOptions().sameOrigin()
        //配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()
        //异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
        //配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
