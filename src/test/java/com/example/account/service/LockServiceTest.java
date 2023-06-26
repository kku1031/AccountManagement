package com.example.account.service;

import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class LockServiceTest {
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @InjectMocks
    private LockService lockService;

    @Test
        void successGetlock() throws InterruptedException {
        //given 어떤 데이터가 있을 때,
        given(redissonClient.getLock(anyString()))
                .willReturn(rLock);
        given(rLock.tryLock(anyLong(),anyLong(),any()))
                .willReturn(true);
        //when 어떤 동작을 하게 되면
        //then 어떤 결과가 나와야한다
        assertDoesNotThrow(() -> lockService.lock("123"));
    }

    @Test
        void failGetLock() throws InterruptedException {
        //given 어떤 데이터가 있을 때,
        given(redissonClient.getLock(anyString()))
                .willReturn(rLock);
        given(rLock.tryLock(anyLong(),anyLong(),any()))
                .willReturn(false);
        //when 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> lockService.lock("123"));
        //then 어떤 결과가 나와야한다
        assertEquals(ErrorCode.ACCOUNT_TRANSACTION_LOCK, exception.getErrorCode());
    }
}