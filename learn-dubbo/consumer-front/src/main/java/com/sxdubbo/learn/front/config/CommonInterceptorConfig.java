package com.sxdubbo.learn.front.config;


import com.sxdubbo.learn.front.Interceptor.CommonInterceptor;
import com.sxdubbo.learn.front.Interceptor.LogNoteInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * created by  luwei
 * 2018-02-28 08:58.
 **/
@Configuration
public class CommonInterceptorConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private LogNoteInterceptor logNoteInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommonInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(logNoteInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/data/**").addResourceLocations("file:C:/data/");
    }
}
