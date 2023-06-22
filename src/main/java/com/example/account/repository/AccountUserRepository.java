package com.example.account.repository;

import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository(DB에 연결할 때 사용하는 인터페이스)가 위치하는 패키지

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
}
