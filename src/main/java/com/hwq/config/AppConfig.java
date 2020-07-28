package com.hwq.config;

import com.hwq.controller.MyFirstInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

//springmvc容器，这是子容器，只扫描Controller注解标注的类
//使用includeFilters时要禁用默认的规则(useDefaultFilters = false),否则不起作用
@ComponentScan(value = "com.hwq",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})},
        useDefaultFilters = false
        )
//开启springmvc功能 ，
@EnableWebMvc
public class AppConfig extends WebMvcConfigurerAdapter { //继承 WebMvcConfigurerAdapter 就可以方便增加各种组件
        //定制

        //视图解析器
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
                //默认所有的页面都从 /WEB-INF/找  xxx.jsp
                //registry.jsp();
                //也可以指定前缀 后缀
                registry.jsp("/WEB-INF/views/",".jsp");
        }

        //静态资源访问
        @Override
        public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
                //这就相当于在配置文件中配置了 <mvc:default-servlet-handler/>
                configurer.enable();
        }

        //增加拦截器

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                // 这样就相当于 配置文件中  <mvc:interceptors></mvc:interceptors>
                // **  表示任意多层的路径
                registry.addInterceptor(new MyFirstInterceptor()).addPathPatterns("/**");
        }
}
