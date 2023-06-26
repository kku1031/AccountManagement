package com.example.account.service;


import com.example.account.aop.AccountLockIdInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    @Around("@annotation(com.example.account.aop.AccountLock) && args(request)") //aspectj문법 :어떤 경우에 적용할 것인가.
    public Object aroundMethod(
            ProceedingJoinPoint pjp,
            AccountLockIdInterface request
    ) throws Throwable {
        //lock 취득 시도
        lockService.lock(request.getAccountNumber());
        try {
            //before : 실제 aop건 부분을 동작 시킴
            return pjp.proceed();
        } finally {
            //after : Lock 해제 (성공하든 실패하든 무조건)
            lockService.unlock(request.getAccountNumber());
        }
    }
}
