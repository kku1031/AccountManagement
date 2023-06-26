package com.example.account.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited  //상속 가능한 구조로 쓰겠다.
public @interface AccountLock {
    long tryLockTime() default 5000L;   //해당 시간 동안 기다려보겠다.
}
