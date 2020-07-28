package com.hwq;

import com.hwq.config.AppConfig;
import com.hwq.config.RootConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//web容器启动的时候创建对象，调用方法初始化容器以及前段控制器
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    //获取根容器的配置类，类似于（spring的配置文件） 父容器
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    //获取web容器的配置类，类似于(springmvc 的配置文件) 子容器
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    //获取DispatcherServlet的映射信息，
    protected String[] getServletMappings() {
        // "/" 拦截所有请求，包括静态资源，例如 xx.js ,xx.png  等等，但不包括 *.jsp
        // "/*" 拦截所有请求，连 *.jsp页面都拦截；jsp页面是tomcat引擎解析的，不应该在这里拦截
        return new String[]{"/"};
    }
}
