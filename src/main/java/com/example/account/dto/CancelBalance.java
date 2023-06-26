package com.example.account.dto;

import com.example.account.aop.AccountLockIdInterface;
import com.example.account.type.TransactionResultType;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class CancelBalance {
    /**
     * {
     * "transactionId":"c2033bb6d82a4250aecf8e27c49b63f6",
     * "accountNumber":"1000000000",
     * "amount":1000
     * }
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request implements AccountLockIdInterface {
        @NotBlank
        private String transactionId;       //난수 : String으로 받음

        @NotNull
        @Size(min = 10, max = 10)
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(1000_000_000)     //최소 거래 금액 10원 ~ 최대 10억원으로 설정.
        private Long amount;   //거래 금액
    }

    /**
     * {
     * "accountNumber":"1000000000",
     * "transactionResult":"S",
     * "transactionId":"5d011bb6d82cc50aecf8e27cdabb6772",
     * "amount":1000,
     * "transactedAt":"2022-06-01T23:26:14.671859"
     * }
     */

    //지금은 UseBalance와 이부분이 같지만 사용하다보면 어디서 UseBalance부분이고 CancelBalance 인지 구분 X -> 따로 구분
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accountNumber;                       //계좌번호
        private TransactionResultType transactionResult;    //잔액사용 결과(성공,실패)
        private String transactionId;                       //잔액 사용 ID
        private Long amount;                                //거래 금액
        private LocalDateTime transactedAt;                 //잔액 사용 일시

        public static Response from(TransactionDto transactionDto) {
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionResult(transactionDto.getTransactionResultType())
                    .transactionId(transactionDto.getTransactionId())
                    .amount(transactionDto.getAmount())
                    .transactedAt(transactionDto.getTransactedAt())
                    .build();
        }
    }
}


