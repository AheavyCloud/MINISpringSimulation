package com.zjh.usage.service;

import com.zjh.spring.annocation_.Component;
import com.zjh.spring.pojo.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class ZJHPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessorAfterInitialzation(Object bean, String beanName) {
        if(beanName.equals("userService")) {

            System.out.println("调用执行初始化后操作···" + beanName + " ");
            // 利用代理模式实现AOP编程！
            // todo 利用工厂模式实现代理模式！
            /**
             *     public static Object newProxyInstance(ClassLoader loader,
             *                                           Class<?>[] interfaces,
             *                                           InvocationHandler h)
             * */

            // 此处的ClassLoader可以用bean的classloader吗？也是可以的，classloader都是一样的ApplicationClassLoader
            Object proxyBean = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 从此处就可以编写切面逻辑！ -- 调用切面的逻辑
                    System.out.println("执行切面逻辑：JDK实现动态模式：" + beanName);
                    Object proxyBean = method.invoke(bean, args); // 执行bean原始方法
                    System.out.println("动态增强！");
                    return proxyBean;

                }
            });
            return proxyBean;
        }
        return bean;
    }
}
