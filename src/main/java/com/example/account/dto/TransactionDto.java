package com.example.account.dto;

import com.example.account.domain.Account;
import com.example.account.domain.Transaction;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Response
public class TransactionDto {

    //원하는 정보 추가, 삭제가 쉬워서 dto로 별도 처리.
    private String accountNumber;
    private TransactionType transactionType;                    //잔액 사용, 취소
    private TransactionResultType transactionResultType;        //거래 성공, 실패
    private Account account;                                    //거래 발생 계좌
    private Long amount;                                        //거래 금액
    private Long balanceSnapshot;                               //거래 후 남은 계좌 잔액
    private String transactionId;                               //거래 고유 ID(거래건 노출 방지)
    private LocalDateTime transactedAt;                         //거래 후 시간, updatedAt에 영향 안받도록 별도 처리

    public static TransactionDto fromEntity(Transaction transaction) {
        return TransactionDto.builder()
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionType(transaction.getTransactionType())
                .transactionResultType(transaction.getTransactionResultType())
                .amount(transaction.getAmount())
                .balanceSnapshot(transaction.getBalanceSnapshot())
                .transactionId(transaction.getTransactionId())
                .transactedAt(transaction.getTransactedAt())
                .build();
    }
}
