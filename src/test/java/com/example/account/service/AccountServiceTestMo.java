//package com.example.account.service;
//
//import com.example.account.domain.Account;
//import com.example.account.type.AccountStatus;
//import com.example.account.repository.AccountRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class) //Mockito 프레임워크가 제공하는 JUnit 5의 확장 클래스
//class AccountServiceTestMo {
//    // AccountRepository의 Mock 객체
//    @Mock
//    private AccountRepository accountRepository;
//
//    // AccountService 객체에 Mock 객체 주입
//    @InjectMocks
//    private AccountService accountService;
//
//    // 계좌 조회 성공을 테스트하는 메서드
//    @Test
//    @DisplayName("계좌 조회 성공")
//    void testXXX() {
//        //given
//        // AccountRepository의 findById 메서드가 특정 계좌 정보를 반환하도록 설정
//        given(accountRepository.findById(anyLong()))
//                .willReturn(Optional.of(Account.builder()
//                        .accountStatus(AccountStatus.UNREGISTERED)
//                        .accountNumber("65789").build()));
//
//        // ArgumentCaptor를 사용하여 findById 메서드에 전달된 파라미터 값을 캡처
//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
//
//        //when
//        // getAccount 메서드 호출하여 반환된 계좌 정보를 확인
//        Account account = accountService.getAccount(4555L);
//
//        //then
//        // findById 메서드가 한 번 호출되었는지 검증
//        verify(accountRepository, times(1)).findById(captor.capture());
//        // save 메서드가 호출되지 않았는지 검증
//        verify(accountRepository, times(0)).save(any());
//        // captor로 캡처한 파라미터 값과 예상값 비교
//        assertEquals(4555L, captor.getValue());
//        assertNotEquals(45515L, captor.getValue());
//        // 계좌 정보의 일부 속성과 예상값 비교
//        assertEquals("65789", account.getAccountNumber());
//        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
//    }
//
//    // 음수로 계좌 조회 시 예외가 발생하는지를 테스트하는 메서드
//    @Test
//    @DisplayName("계좌 조회 실패 - 음수로 조회")
//    void testFailedToSearchAccount() {
//        //given
//
//        //when
//        // 음수 값을 전달하여 getAccount 메서드 호출 시 RuntimeException 예외가 발생하는지 검증
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> accountService.getAccount(-10L));
//
//        //then
//        // 예외 메시지와 예상값 비교
//        assertEquals("Minus", exception.getMessage());
//    }
//
//    // 계좌 조회 결과를 확인하는 단순한 테스트 메서드
//    @Test
//    @DisplayName("Test 이름 변경")
//    void testGetAccount() {
//        //given
//        // AccountRepository의 findById 메서드가 특정 계좌 정보를 반환하도록 설정
//        given(accountRepository.findById(anyLong()))
//                .willReturn(Optional.of(Account.builder()
//                        .accountStatus(AccountStatus.UNREGISTERED)
//                        .accountNumber("65789").build()));
//
//        //when
//        // getAccount 메서드 호출하여 반환된 계좌 정보를 확인
//        Account account = accountService.getAccount(4555L);
//
//        //then
//        // 계좌 정보의 일부 속성과 예상값 비교
//        assertEquals("65789", account.getAccountNumber());
//        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
//    }
//
//    // 계좌 조회 결과를 확인하는 단순한 테스트 메서드
//    @Test
//    void testGetAccount2() {
//        //given
//        // AccountRepository의 findById 메서드가 특정 계좌 정보를 반환하도록 설정
//        given(accountRepository.findById(anyLong()))
//                .willReturn(Optional.of(Account.builder()
//                        .accountStatus(AccountStatus.UNREGISTERED)
//                        .accountNumber("65789").build()));
//
//        //when
//        // getAccount 메서드 호출하여 반환된 계좌 정보를 확인
//        Account account = accountService.getAccount(4555L);
//
//        //then
//        // 계좌 정보의 일부 속성과 예상값 비교
//        assertEquals("65789", account.getAccountNumber());
//        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
//    }
//}
