package com.example.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor

//RedisRepositoryConfig의 client 사용을 위한 클래스.
public class RedisTestService {
    //자동 생성자 주입
    private final RedissonClient redissonClient; //bean 이랑 이름 같으니까 주입됨

    //SpinLock 실습
    public String getLock() {
        RLock lock = redissonClient.getLock("sampleLock");

        try {                           //1초 동안 락 기다림, 성공하면 5초 지난 후에 풀어줌
            boolean isLock = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if(!isLock) {
                log.error("=============Lock acquisition failed ==============");
                return "Lock failed";
            }
        } catch (Exception e) {
            log.error("Redis lock failed");
        }

        return "lock success";
    }
}
