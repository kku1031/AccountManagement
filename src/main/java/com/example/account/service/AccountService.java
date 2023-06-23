package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.example.account.type.AccountStatus.IN_USE;

@Service //서비스타입 빈으로 스프링을 자동으로 등록을 해주기 위해서!
@RequiredArgsConstructor //꼭 필요한 argument가 들어간 생성자를 만들어줌. final 타입만 담긴 생성자 형성

//AccountRepository 사용해서 의존성 주입하여 데이터 저장하는 로직.
public class AccountService {
    //final들어가면 생성자만 받을 수 있음.(과거 autowired로 주입했음)
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /**
     * 사용자가 있는지 조회
     * 계좌의 번호를 생성하고
     * 계좌 저장, 그 정보를 넘김.
     *
     * @return
     */
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance) {
        AccountUser accountUser = accountUserRepository.findById(userId)    //유저 아이디 조회
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND)); //사용자가 없을 때
        //AccountUser 계좌 count
        validateCreateAccount(accountUser);

        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc() //최근에 생성된 계좌
                .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "") //계좌가 있으면 +1 후 문자로 변경
                .orElse("1000000000"); //계좌가 없으면 최초생성

        //builder로 account 계좌 저장
        //Account(Entity) -> accountRepository에 저장 -> 그러고 나온 entity
        //-> fromEntity에 저장 -> fromEntity의 Entity -> AccountDto에 저장
        //-> Controller에서 받아서 사용
        return AccountDto.fromEntity(
                accountRepository.save(Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(IN_USE)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build())
        );
    }

    private void validateCreateAccount(AccountUser accountUser) {
        if (accountRepository.countByAccountUser(accountUser) >= 10) {
            throw new AccountException(ErrorCode.MAX_ACCOUNT_PER_USER_10);
        }
    }

    //생성된거 DB로 가져옴(select)
    @Transactional
    public Account getAccount(Long id) {
        if (id < 0) {
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();

    }

    //계좌 해지
    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber) {
        AccountUser accountUser = accountUserRepository.findById(userId)                //유저 아이디 조회
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));     //사용자가 없을 때
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));  //계좌가 없을 때,
        validateDeleteAccount(accountUser, account);

        //계좌 해지 후 -> 상태 업데이트, 시간등록
        account.setAccountStatus(AccountStatus.UNREGISTERED);
        account.setUnRegisteredAt(LocalDateTime.now());

        return AccountDto.fromEntity(account);
    }

    private void validateDeleteAccount(AccountUser accountUser, Account account) {
        if (accountUser.getId() != account.getAccountUser().getId()) {
            throw new AccountException(ErrorCode.USER_ACCOUNT_UN_MATCH) ;            //사용자 아이디, 계좌 소유주가 다른 경우
        }
        if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED) ;     //계좌가 이미 해지 상태인 경우
        }
        if (account.getBalance() > 0) {
            throw new AccountException(ErrorCode.BALANCE_NOT_EMPTY);                //잔액이 있는 경우 실패 응답
        }
    }
}
