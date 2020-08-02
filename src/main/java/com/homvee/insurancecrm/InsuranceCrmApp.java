package com.homvee.insurancecrm;

import com.homvee.insurancecrm.web.interceptors.UsrAuthInterceptor;
import com.homvee.insurancecrm.web.interceptors.UsrLoginInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.homvee.insurancecrm.dao"})
@EntityScan(basePackages = {"com.homvee.insurancecrm.dao.entities"})
@ComponentScan(basePackages = {"com.homvee"})
public class InsuranceCrmApp implements WebMvcConfigurer {

    @Resource
    private UsrLoginInterceptor usrLoginInterceptor;
    @Resource
    private UsrAuthInterceptor usrAuthInterceptor;
    public static void main(String[] args) {
        SpringApplication.run(InsuranceCrmApp.class, args);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(usrLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/")
        ;
        registry.addInterceptor(usrAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login/**")
                .excludePathPatterns("/user/setting")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/")
        ;

    }

}
