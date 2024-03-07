package com.zjh.spring.pojo;

public class BeanDefinition {
    // bean类型
    private Class type;

    // bean是原型bean还是单例bean 创建多次还是一次？
    private String scope;

    // 此bean是否是懒加载模式的
    private boolean isLazy;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
