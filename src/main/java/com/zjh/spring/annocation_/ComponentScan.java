package com.zjh.spring.annocation_;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 在何处生效：运行时之前即可生效
@Target(ElementType.TYPE) // 作用域：作用于类上
public @interface ComponentScan {
    String value() default "";
}
