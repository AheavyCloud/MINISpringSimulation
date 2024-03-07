package com.zjh.usage.service;

import com.zjh.spring.annocation_.Component;
import com.zjh.spring.annocation_.Scope;

@Component
@Scope("singleton")
public class UserService {

    public void doSomething(){
        System.out.println("我已经被IOC容器创建并管理啦···");
    }
}
