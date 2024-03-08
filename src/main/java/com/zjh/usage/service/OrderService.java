package com.zjh.usage.service;

import com.zjh.spring.annocation_.Component;
import com.zjh.spring.annocation_.ComponentScan;
import com.zjh.spring.annocation_.Scope;

@Component
@Scope
public class OrderService implements Service {
    public void doSomethings(){
        System.out.println("order!我被IOC创建啦！！！");
    }
}
