# 模拟MINI Spring启动流程
    Spring有两大特性：IOC与AOP，IOC实现控制反转，AOP实现代码增强
    spring 初始化流程
    Component扫描 --> 保存BeanDefinition至 BeanDefinitionMap中 -> 实例化对象 -> 属性赋值
    -> 初始化前 -> 初始化 -> 初始化后 -> 使用 -> 卸载

## IOC容器：
    ApplicationContext:
        IOC容器的入口，
    通过此容器可以扫描需要生成的Bean对象、创建Bean对象、最后获取bean对象
    IOC在启动的过程中需要加载非懒加载的单例Bean
    将所有bean对象封装到BeanDefinition中，保存到BeanDefinitionMap中
    扫描完所有的bean对象以后，对单例bean进行创建
    同时判断，此bean是否是BeanPostProcessor的是实现类，如果是其实现类则创建并添加到beanPostProcessorList集合中

    完成crateBean()方法
    createBean()方法通过从BeanDefinition中获取的class对象，通过反射机制创建bean对象
    如果判断是单例bean，那么就将其放入到单例池SingletonObjectMap中保存，
    context容器getBean()方法时，如果是单例模式就从单例池中查找，如果不是单例就调用createBean()创建
## 依赖注入
### 1. 创建Bean对象时寻找@autowired注解的字段

    在crateBean方法内创建完对象实例以后（利用无参构造方法实例化对象以后），
    寻找实例对象内部的属性值是否有@Autowired注解，
    含有此注解则完成步骤2
### 2. 对含有@Autowired注解的字段进行依赖注入
    1. 寻找单例池对象是否被实现
        查找方式：singletonObjects中先byType再byName查找
        单例池中含有此对象则说明此对象是单例对象，直接赋值
    2，单例池中没有此对象
        则说明，此对象是1.原型bean对象 2. 未创建的单例对象
        调用getBean()方法进行bean对象的创建
#### 循环依赖问题就由此呈现
        
    当实例化完对象以后，需要对含有@Autowired注解的对象属性进行属性赋值，在本案例中就是
    对userService对象中的 orderService属性进行赋值，
    此时userService对象已经完成了实例化操作，但是还没有完全交给IOC容器进行管理,没有交给单例池singletonObjects，orderService对象在此时仍未创建
    如果OrderService类定义中也含有属性UserService，并且也加入和@Autowired注解，那么循环引用问题由此产生
    在代码中则是
        createBean(userService) -> getBean(orderService) --> createBean(orderService) 
        --> getBean(userService) --> createBean(userService)
    的死循环
#### todo 解决循环依赖问题
    在Spring源码中使用三级缓存机制来解决循环依赖的问题，
    本质上是因为，虽然userService对象还没有进入单例池singletonObjects由IOC容器统一管理，但是userService已经完成了实例化，
    在内存中已经有了地址

## 初始化操作 InitialingBean
    当Bean对象实现了InitialingBean接口，就可以在属性赋值之后执行初始化操作
## BeanPostProcessor！
spring源码
```java
public interface BeanPostProcessor {
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
    
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
```

    在Spring源码中，使用到了增强骑BeanPostProcessor 其中内含了两个方法：
    初始化前和初始化后！
### 具体用法
    用户可以自己编写类，实现此接口，从而可以在初始化前或者初始化后执行此操作！
Spring如何得知用户是否自己编写了此接口的实现类呢？

    在编写此类的上面也使用@Coomponent注解，也是通过多态理论中的父类指针指向子类对象的方式完成，调用此方法的初始化前和初始化后操作
    同时因为BeanPostProcessor是在初始化前后进行执行的，所以，有几个Bean创建的过程，就会导致BeanPostProcessor被执行几次
    那么在本案例中一共有3次执行了此方法：
    分别是：ZJHPostProcessor初始化后，OrderService初始化后，以及UserService初始化后
        调用执行初始化后操作···ZJHPostProcessor 
#### 执行结果
    调用执行初始化后操作···orderService
    userService执行初始化操作
    调用执行初始化后操作···userService
    order!我被IOC创建啦！！！
    UserService已经被IOC容器创建并管理啦···
#### 细节，如果想针对某一个Bean对象执行初始化后操作则：
    在自己定义的ZJHPostProcessor中加入判断语句
    并且通过传入参数
    ZJHPostProcessor(Object bean, String beanName)
    以及使用代理模式Proxy.getProxyInstance拿到代理对象！
### 注解实现普通成员属性赋值
    就是通过实现BeanPostProcessor接口，通过使用befor or after方式对其他属性完成赋值！
## 下版本任务
    编写Aware接口并实现注意机制
    需求：
        获取bean的名字，注入到bean对象中，实现bean对象中维护beanName属性，使得创建好bean对象以后
    其属性beanName是自己在单例池中的名字
    什么时候就会创建了自己的beanName呢？
    bean生命周期：
    扫描所有的@Conmponet注解 -> 生成BeanDefinition对象 -> 放入BeanDefinitionMap -> 根据bean类型进行实例化
    -> 属性赋值 -> 执行Aware接口方法 -> 初始化前 -> 初始化 -> 初始化后（代理模式AOP）

    为什么使用Aware回调机制？
    使用Aware回调机制的原因在于，Spring自动创建了Bean对象，如果我们想知道bean对象的相关信息：
    例如 bean的名字：beanName，或者是什么工厂创建了此Bean：BeanFactory等信息，那么就可以实现Aware接口
    通过在属性赋值后，初始化前判断bean是否实现了Aware接口而将所需要的信息传递给bean对象。
