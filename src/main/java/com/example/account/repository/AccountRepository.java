package com.example.account.repository;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Entity를 DB에 저장해주기 위해서는 JPA에서 제공해주는 repository가 필요
//특수한 형태의 인터페이스 : spring data jpa에서 spring쪽에서 jpa를 더 쓰기 쉽게 만들어주는 기능.

@Repository //Account라는 테이블에 접속하기 위한 Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //최근에 추가된 계좌조회 : 값이 있을 수도 없을 수도 있으니 Optional
    Optional<Account> findFirstByOrderByIdDesc();

    //1인당 계좌 개수를 조회 : Account안에 accountUser를 연관관계로 가지고 있기 때문에 가능
    Integer countByAccountUser(AccountUser accountUser);

    //계좌번호 찾기 : Account의 accountNumber에서
    Optional<Account> findByAccountNumber(String AccountNumber);

}
