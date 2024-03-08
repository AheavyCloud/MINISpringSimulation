# 模拟MINI Spring启动流程
    Spring有两大特性：IOC与AOP，IOC实现控制反转，AOP实现代码增强
## IOC容器：
    ApplicationContext:
        IOC容器的入口，
    通过此容器可以扫描需要生成的Bean对象、创建Bean对象、最后获取bean对象
    IOC在启动的过程中需要加载非懒加载的单例Bean
    将所有bean对象封装到BeanDefinition中，保存到BeanDefinitionMap中
    扫描完所有的bean对象以后，对单例bean进行创建
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
    在内存中已经有了地址，
        