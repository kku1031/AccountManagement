package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
    @DisplayName("계좌생성 성공")
    void createAccountSuccess() {
        // given: 어떤 데이터가 있을 때
        AccountUser user = AccountUser.builder()
                .name("Pobi").build();
        user.setId(12L);
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
    @DisplayName("계좌정보가 없는 경우 - 계좌 생성 실패")
    void createFirstAccount() {
        // given: 어떤 데이터가 있을 때
        AccountUser user = AccountUser.builder()
                .name("Pobi").build();
        user.setId(15L);
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
    @DisplayName("한명당 계좌가 10개인 경우 - 계좌 생성 실패")
    void createAccount_maxAccountIs10() {
        //given 어떤 데이터가 있을 때,
        AccountUser user = AccountUser.builder()
                .name("Pobi").build();
        user.setId(12L);
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

    @Test
    @DisplayName("계좌 삭제 성공 테스트")
    void deleteAccountSuccess() {
        // given: 어떤 데이터가 있을 때
        AccountUser user = AccountUser.builder()
                .name("Pobi").build();
        user.setId(12L);
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountUser(user)
                        .balance(0L)
                        .accountNumber("1000000012").build()));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        // when: 어떤 동작을 하게 되면 (응답값)
        AccountDto accountDto = accountService.deleteAccount(1L, "1234567890");

        // then: 어떤 결과가 나와야 함
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(12L, accountDto.getUserId());
        assertEquals("1000000012", captor.getValue().getAccountNumber());
        assertEquals(AccountStatus.UNREGISTERED, captor.getValue().getAccountStatus());
    }

    @Test
    @DisplayName("해당 유저 없음 - 계좌 해지 실패")
    void deleteAccount_UserNotFound() {
        // given: 어떤 데이터가 있을 때
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // when: 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.deleteAccount(1L, "1234567890"));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("해당 계좌 없음 - 계좌 해지 실패")
    void deleteAccount_AccountNotFound() {
        // given: 어떤 데이터가 있을 때
        AccountUser user = AccountUser.builder()
                .name("Pobi").build();
        user.setId(12L);
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.empty());

        // when: 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.deleteAccount(1L, "1234567890"));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("계좌 소유주가 다를 때")
    void deleteAccountFailed_userUnMatch() {
        // given: 어떤 데이터가 있을 때
        AccountUser pobi = AccountUser.builder()
                .name("pobi").build();
        pobi.setId(12L);
        AccountUser Pikachu = AccountUser.builder()
                .name("Pikachu").build();
        pobi.setId(13L);
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountUser(Pikachu)
                        .balance(0L)
                        .accountNumber("1000000012").build()));
        // when: 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.deleteAccount(1L, "1234567890"));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.USER_ACCOUNT_UN_MATCH, exception.getErrorCode());
    }

    @Test
    @DisplayName("해지 계좌에 잔액이 남아 있을 때")
    void deleteAccountFailed_balanceNotEmpty() {
        // given: 어떤 데이터가 있을 때
        AccountUser pobi = AccountUser.builder()
                .name("Pobi").build();
        pobi.setId(12L);
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountUser(pobi)
                        .balance(100L)                 //잔액 100원 남음.
                        .accountNumber("1000000012").build()));
        // when: 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.deleteAccount(1L, "1234567890"));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.BALANCE_NOT_EMPTY, exception.getErrorCode());
    }

    @Test
    @DisplayName("이미 해지된 계좌인 경우")
    void deleteAccountFailed_alreadyUnregistered() {
        // given: 어떤 데이터가 있을 때
        AccountUser pobi = AccountUser.builder()
                .name("Pobi").build();
        pobi.setId(12L);
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountUser(pobi)
                        .accountStatus(AccountStatus.UNREGISTERED) //이미 계좌해지됨
                        .balance(0L)
                        .accountNumber("1000000012").build()));
        // when: 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.deleteAccount(1L, "1234567890"));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED, exception.getErrorCode());
    }

    @Test
        void successGetAccountsByUserId() {
        //given 어떤 데이터가 있을 때,
        AccountUser pobi = AccountUser.builder()
                .name("Pobi").build();
        pobi.setId(12L);
        //계좌 3개 생성
        List<Account> accounts = Arrays.asList(
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("1111111111")
                        .balance(1000L)
                        .build(),
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("2222222222")
                        .balance(2000L)
                        .build(),
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("3333333333")
                        .balance(3000L)
                        .build()
        );
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));
        given(accountRepository.findByAccountUser(any()))
                .willReturn(accounts);
        //when 어떤 동작을 하게 되면
        List<AccountDto> accountDtos = accountService.getAccountsByUserId(1L);
        //then 어떤 결과가 나와야한다
        assertEquals(3, accountDtos.size());
        assertEquals("1111111111",accountDtos.get(0).getAccountNumber());
        assertEquals(1000,accountDtos.get(0).getBalance());
        assertEquals("2222222222",accountDtos.get(1).getAccountNumber());
        assertEquals(2000,accountDtos.get(1).getBalance());
        assertEquals("3333333333",accountDtos.get(2).getAccountNumber());
        assertEquals(3000,accountDtos.get(2).getBalance());
    }

    @Test
    @DisplayName("계좌확인 - 유저 정보가 없을 때,")
    void failedToGetAccounts() {
        //given 어떤 데이터가 있을 때,
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // when: 어떤 동작을 하게 되면
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.getAccountsByUserId(1L));

        // then: 어떤 결과가 나와야 함
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}
