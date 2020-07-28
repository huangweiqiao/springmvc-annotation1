package com.hwq.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
//spring容器，这是父容器，这个容器不扫描Controller注解标注的类
@ComponentScan(value = "com.hwq",excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})})
public class RootConfig {
}
