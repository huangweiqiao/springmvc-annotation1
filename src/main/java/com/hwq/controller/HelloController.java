package com.hwq.controller;


import com.hwq.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @Autowired
    private HelloService helloService;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        String hello = helloService.sayHello("tomcat...");
        return hello;
    }

    @RequestMapping("/suc")
    public String suc(){
        //这个样的话，这个方法就会返回到  /WEB-INF/views/success.jsp ，因为 我们在 AppConfig里指定了 前缀和后缀，springmvc会将前缀和这个返回值在加上后缀拼起来
        return "success";
    }


}
