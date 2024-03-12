package com.zjh.usage.service;

import com.zjh.spring.annocation_.Autowired;
import com.zjh.spring.annocation_.Component;
import com.zjh.spring.annocation_.Scope;
import com.zjh.spring.annocation_.ValueImport;
import com.zjh.spring.pojo.BeanNameAware;
import com.zjh.spring.pojo.InitialingBean;

@Component
@Scope("singleton")
public class UserService implements InitialingBean, Service, BeanNameAware {

    @Autowired
    private OrderService orderService;

    private String beanName;

    @ValueImport("我被赋值啦")
    private String testValue ;
    public void doSomethings(){
        orderService.doSomethings();
        System.out.println("UserService已经被IOC容器创建并管理啦···");
        System.out.println("属性赋值结果为：" + testValue);
        System.out.println("beanName是：" + beanName);

    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("userService执行初始化操作");

    }
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
