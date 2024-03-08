package com.zjh;

import com.zjh.spring.ZJHApplicationContext;
import com.zjh.spring.annocation_.Autowired;
import com.zjh.usage.AppConfig;
import com.zjh.usage.service.OrderService;
import com.zjh.usage.service.Service;
import com.zjh.usage.service.UserService;

public class Main {
    public static void main(String[] args) throws Exception {

        // 创建IOC容器，获取配置类信息 创建单例Bean
        ZJHApplicationContext zjhApplicationContext = new ZJHApplicationContext(AppConfig.class);

        // 由于根据接口进行动态代理过了，因此此处应该强制转换成Service对象
        Service userService =(Service) zjhApplicationContext.getBean("userService");

        userService.doSomethings();

//        OrderService orderService =(OrderService) zjhApplicationContext.getBean("orderService");
//        orderService.doSomethings();

    }
}