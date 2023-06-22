//package com.example.account.service;
//
//import com.example.account.domain.Account;
//import com.example.account.type.AccountStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest //build.gradle에 spring-boot-starter-test에서 가능
////Bean들이 다 포함되어 있음 -> 이말은 = 주입이 가능 -> Autowired 사용가능
//class AccountServiceTest {
//    @Autowired
//    private AccountService accountService;
//
//    //무조건 밑에꺼 테스트 하기 전에 이거 먼저 작동(사전에 데이터 저장)
//    @BeforeEach
//    void init() {
//        accountService.createAccount();
//    }
//
//    @Test
//    @DisplayName("Test 이름 변경")
//    void testGetAccount() {
//        Account account = accountService.getAccount(1L);
//        assertEquals("40000", account.getAccountNumber());
//        assertEquals(AccountStatus.IN_USE, account.getAccountStatus());
//    }
//
//    @Test
//    void testGetAccount2() {
//        Account account = accountService.getAccount(2L);
//        assertEquals("40000", account.getAccountNumber());
//        assertEquals(AccountStatus.IN_USE, account.getAccountStatus());
//    }
//    //문제점 :
//    // 1. 사전에 데이터를 만들어야하고
//    // 2. 상황이 맞지 않으면 동일한 테스트인데도 어떨땐 맞고, 어떨땐 틀림.
//    // 3. 다양한 데이터를 미리 만들어야하는 상황이면, 사전 준비 과정이 까다로워짐
//    //=> Mockito : Mocking을 통한 격리성을 확보해주는 test 라이브러리 중에 하나.
//}