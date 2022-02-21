package com.lyl.bysj.config;

import com.lyl.bysj.interceptors.AdminInterceptor;
import com.lyl.bysj.interceptors.DoctorInterceptor;
import com.lyl.bysj.interceptors.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfig getWebMvcConfig(){
        return new WebMvcConfig(){

            @Override
            public void addViewControllers(ViewControllerRegistry registry){

            }

            @Override
            public void addInterceptors(InterceptorRegistry registry){
                registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin").excludePathPatterns("/admin/login");
                registry.addInterceptor(new DoctorInterceptor()).addPathPatterns("/doctor").excludePathPatterns("/doctor/login");
                registry.addInterceptor(new UserInterceptor()).addPathPatterns("/user").excludePathPatterns("/user/login");
            }
        };
    }
}
