package com.zjh.spring;

import com.zjh.spring.annocation_.Component;
import com.zjh.spring.annocation_.ComponentScan;
import com.zjh.spring.annocation_.Scope;
import com.zjh.spring.pojo.BeanDefinition;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ZJHApplicationContext {
    private Class configClass;

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonObjects = new HashMap<>(); // 单例池：用于存放实际创建出来的bean对象！
    public ZJHApplicationContext(Class configClass) throws Exception {
        this.configClass = configClass;

        // 对路径以及文件扫描，并将bean对象存入beanDefinitionMap中保存起来
        scan(configClass);

        // 扫描完所有的数据以后，将所有的对象存入map中，此时创建所有的单例Bean

        for(Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()){
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if(beanDefinition.getScope().equals("singleton")){
                // 单例模式： --> 如何创建单例模式？bean对象本身不是单例的，
                // 创建对象,保存进单例池
                Object singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }

        }

    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) throws Exception{
        Class clazz = beanDefinition.getType();
        Object o = clazz.getConstructor().newInstance();
        return o;
    }


    private void scan(Class configClass) {
        // 扫描路径： 判断当前的configClass是否有ComponentScan注解
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScan =(ComponentScan) configClass.getAnnotation(ComponentScan.class);
            // 获取此注解的值！
            String path = componentScan.value(); // com.zjh.usage --> 获取相对路径
            ClassLoader classLoader = this.getClass().getClassLoader();
            // 文件名修改
            path = path.replace(".", "/"); // com.zjh.usage --> com/zjh/usage

            // 获取文件目录 -- 绝对路径 --> 通过绝对路径获取class文件的绝对路径！
            URL resource = classLoader.getResource(path);

            // 目录也是一个文件
            File dict = new File(resource.getFile());
            if(dict.isDirectory()){
                // 遍历目录下的文件
                for (File file : dict.listFiles()){
                    String absolutePath = file.getAbsolutePath();
                    // 截取数据：成为
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                    absolutePath = absolutePath.replace("\\", "."); // --> com.zjh.usage.service.OrderService
//                    System.out.println(absolutePath);
                    // 将文件遍历出来以后，判断是否为Component对象然后将符合的加载成为一个class对象
                    try {
                        Class<?> clazz = classLoader.loadClass(absolutePath);
                        //  生成class对象, 将Bean对象信息保存进入BeanDefinition类中，

                        if(clazz.isAnnotationPresent(Component.class)){
                            // 是bean对象，
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);

                            // 判断是否是sington单例bean还是原型bean
                            if(clazz.isAnnotationPresent(Scope.class)){
                                Scope scope = clazz.getAnnotation(Scope.class);
                                String beanScope = scope.value();
                                beanDefinition.setScope(beanScope);

                            }
                            else { // 没有scope注解默认是单例模式
                                beanDefinition.setScope("singleton");

                            }

                            // 获取Componet对象的参数，作为map的key值
                            Component component = clazz.getAnnotation(Component.class);
                            String beanName = component.value();
                            if(beanName.equals("")){
                                // todo 当值为空时，自定义注解名称
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                            else { // 值不为空则存入beanDefinitionMap集合中！
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }

                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }
            }




        }
    }

    // IOC容器获取bean对象
    public Object getBean(String beanName) throws Exception {
        if(!beanDefinitionMap.containsKey(beanName)){
            // map中没有bean的名字，那么就根本没有定义这个异常
            throw new Exception();
        }
        // 有bean对象：
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        // 判断是单例bean还是原型bean
        if(beanDefinition.getScope().equals("protoType")){
            // 创建原型bean对象, 每一次调用getBean都会返回bean对象
            Object bean = createBean(beanName, beanDefinition);
            return bean;
        }
        else{ // 单例bean状态下：
            return singletonObjects.get(beanName);
        }

    }

}
