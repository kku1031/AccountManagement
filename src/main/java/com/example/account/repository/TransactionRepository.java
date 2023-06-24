package com.example.account.repository;

import com.example.account.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Transaction라는 테이블에 접속하기 위한 Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
