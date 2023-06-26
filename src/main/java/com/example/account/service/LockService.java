package com.example.account.service;

import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
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
public class LockService {
    //자동 생성자 주입
    private final RedissonClient redissonClient; //bean 이랑 이름 같으니까 주입됨

    //SpinLock 실습
    public void lock(String accountNumber) {
        //계좌번호 : lock의 키로
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        log.debug("Trying lock for accountNumber : {}", accountNumber);

        try {                           //1초 동안 락 기다림, 성공하면 5초 지난 후에 풀어줌
            boolean isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);
            if (!isLock) {
                log.error("=============Lock acquisition failed ==============");
                throw new AccountException(ErrorCode.ACCOUNT_TRANSACTION_LOCK);
            }
        } catch (AccountException e) {
            throw e;
        } catch (Exception e) {
            log.error("Redis lock failed");
        }
    }

    public void unlock(String accountNumber) {
        log.debug("unlock for accountNumber : {} ", accountNumber);
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }
    private String getLockKey(String accountNumber) {
        return "ACLK : " + accountNumber;
    }
}
