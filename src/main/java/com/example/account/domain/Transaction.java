package com.example.account.domain;

import com.example.account.type.AccountStatus;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;              //잔액 사용, 취소
    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;  //거래 성공, 실패

    @ManyToOne
    private Account account;                              //거래 발생 계좌
    private Long amount;                                  //거래 금액
    private Long balanceSnapshot;                         //거래 후 남은 계좌 잔액

    private String transactionId;                         //거래 고유 ID(거래건 노출 방지)
    private LocalDateTime transactedAt;                   //거래 후 시간, updatedAt에 영향 안받도록 별도 처리

}

