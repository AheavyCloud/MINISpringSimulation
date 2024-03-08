package com.zjh.usage.service;

import com.zjh.spring.annocation_.Component;
import com.zjh.spring.annocation_.ValueImport;
import com.zjh.spring.pojo.BeanPostProcessor;
import lombok.val;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class ValuePostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitialzation(Object bean, String beanName) throws IllegalAccessException {

        for (Field field : bean.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(ValueImport.class)){
                String value = field.getAnnotation(ValueImport.class).value();
                field.setAccessible(true);
                field.set(bean, value);
            }
        }

        return bean;
    }
}
