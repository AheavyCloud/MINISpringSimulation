package com.zjh;

import com.zjh.spring.ZJHApplicationContext;
import com.zjh.usage.AppConfig;
import com.zjh.usage.service.UserService;

public class Main {
    public static void main(String[] args) {

        // 创建IOC容器，获取配置类信息 创建单例Bean
        ZJHApplicationContext zjhApplicationContext = new ZJHApplicationContext(AppConfig.class);
        UserService userService =(UserService) zjhApplicationContext.getBean("userService");


    }
}