package com.zjh.usage.service;

import com.zjh.spring.annocation_.Autowired;
import com.zjh.spring.annocation_.Component;
import com.zjh.spring.annocation_.Scope;

@Component
@Scope("singleton")
public class UserService {

    @Autowired
    private OrderService orderService;
    public void doSomething(){
        orderService.doSomethings();
        System.out.println("UserService已经被IOC容器创建并管理啦···");
    }
}
