# 模拟MINI Spring启动流程
    Spring有两大特性：IOC与AOP，IOC实现控制反转，AOP实现代码增强
## IOC容器：
    ApplicationContext:
        IOC容器的入口，
    通过此容器可以扫描需要生成的Bean对象、创建Bean对象、最后获取bean对象
    IOC在启动的过程中需要加载非懒加载的单例Bean
        