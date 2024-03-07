# usage包
    用于存放测试所需的类
    usage
        |-service
            |- UserService
        |-AppConfig
## service
    service 包用于存放具体需要交给IOC容器管理的测试对象
    UserService：被IOC容器管理的对象
## AppConfig
    用于向IOC容器传递配置信息，例如包扫描路径等
    通过定义的@Component("path")获取待扫描的路径