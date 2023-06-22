package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;

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

    //생성된거 DB로 가져옴(select)
    @Transactional
    public Account getAccount(Long id) {
        if (id < 0) {
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();

    }
}
