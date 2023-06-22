package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    // 2개의 Mock이 달려있는 AccountService가 생성
    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountSuccess() {
        // given: 어떤 데이터가 있을 때
        AccountUser user = AccountUser.builder()
                .id(12L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user)); // AccountService의 user 객체를 반환하도록 설정
        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.of(Account.builder()
                        .accountNumber("1000000012").build())); // AccountService의 계좌번호 객체 반환
        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("1000000015").build()); // AccountService 유저와 계좌번호 전부 저장되있음.

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        // when: 어떤 동작을 하게 되면 (응답값)
        AccountDto accountDto = accountService.createAccount(1L, 1000L); // createAccount() 메서드 호출

        // then: 어떤 결과가 나와야 함

        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(12L, accountDto.getUserId()); // accountDto.getUserId()의 값이 12L과 일치하는지 검증
        assertEquals("1000000013", captor.getValue().getAccountNumber()); // accountDto.getAccountNumber()의 값이 "1000000013"과 일치하는지 검증
    }

    @Test
    @DisplayName("계좌정보가 없는 경우 - 계좌 생성 실패")
    void createFirstAccount() {
        // given: 어떤 데이터가 있을 때
        AccountUser user = AccountUser.builder()
                .id(15L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user)); // AccountService의 user 객체를 반환하도록 설정
        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.empty());
        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("1000000015").build()); // AccountService 유저와 계좌번호 전부 저장되있음.
        //실제로 저장되는 데이터는 captor 안에
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        // when: 어떤 동작을 하게 되면 (응답값)
        AccountDto accountDto = accountService.createAccount(1L, 1000L); // createAccount() 메서드 호출

        // then: 어떤 결과가 나와야 함
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(15L, accountDto.getUserId()); // accountDto.getUserId()의 값이 12L과 일치하는지 검증
        assertEquals("1000000000", captor.getValue().getAccountNumber()); // accountDto.getAccountNumber()의 값이 "1000000013"과 일치하는지 검증
    }

    @Test
    @DisplayName("유저가 없는 경우 - 계좌 생성 실패")
    void createAccount_UserNotFound() {
        // given: 어떤 데이터가 있을 때
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty()); // AccountService의 user 객체를 반환하도록 설정
        // when: 어떤 동작을 하게 되면 (응답값)
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("한명당 계좌가 10개인 경우 - 계좌 생성 실패")
        void createAccount_maxAccountIs10() {
        //given 어떤 데이터가 있을 때,
        AccountUser user = AccountUser.builder()
                .id(15L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.countByAccountUser(any()))
                .willReturn(10);
        // when: 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.MAX_ACCOUNT_PER_USER_10, exception.getErrorCode());
    }
}
