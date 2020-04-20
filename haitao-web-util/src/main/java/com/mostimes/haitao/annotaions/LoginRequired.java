package com.mostimes.haitao.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//使用范围：只在方法有效
@Retention(RetentionPolicy.RUNTIME)//生效范围：在虚拟机运行时有效
public @interface LoginRequired {

    boolean loginSuccess() default true;
}
