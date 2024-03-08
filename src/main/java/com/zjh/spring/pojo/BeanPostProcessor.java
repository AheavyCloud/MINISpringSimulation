package com.zjh.spring.pojo;

public interface BeanPostProcessor {
    default Object postProcessorBeforeInitialzation(Object bean, String beanName) throws IllegalAccessException {
        return bean;
    }

    default Object postProcessorAfterInitialzation(Object bean, String beanName){
        return bean;
    }
}
