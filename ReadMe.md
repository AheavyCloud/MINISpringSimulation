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
    