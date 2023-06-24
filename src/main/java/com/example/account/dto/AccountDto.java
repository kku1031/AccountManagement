package com.example.account.dto;

import com.example.account.domain.Account;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    //Entity 클래스와 비슷한데 필요한 부분만 넣어둬서 응답에 필요한 데이터 처리
    //Controller와 Service간에 데이터 주고 받는데 최적화된 Dto
    private Long userId;
    private String accountNumber;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    //특정타입으로 바꿔줄때 static 메소드의 생성자 활용하여 만듬.
    //(특정 Entity에서 특정 Dto로 변환해줄 때)
    public static AccountDto fromEntity(Account account) {
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .registeredAt(account.getRegisteredAt())
                .unRegisteredAt(account.getUnRegisteredAt())
                .build();
    }
}
