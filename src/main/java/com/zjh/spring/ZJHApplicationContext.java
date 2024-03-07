package com.zjh.spring;

import com.zjh.spring.annocation_.ComponentScan;

import java.lang.annotation.Annotation;
import java.net.URL;

public class ZJHApplicationContext {
    private Class configClass;

    public ZJHApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描路径： 判断当前的configClass是否有ComponentScan注解
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScan =(ComponentScan) configClass.getAnnotation(ComponentScan.class);
            // 获取此注解的值！
            String path = componentScan.value(); // com.zjh.usage --> 获取相对路径
            ClassLoader classLoader = this.getClass().getClassLoader();
            // 文件名修改
            path = path.replace(".", "/"); // com.zjh.usage --> com/zjh/usage

            // 获取文件目录
            URL resource = classLoader.getResource(path);

            System.out.println(resource);

        }

    }

    // IOC容器获取bean对象
    public Object getBean(String beanName){
        return null;
    }

}
