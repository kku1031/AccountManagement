package com.example.account.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateAccount {
    @Getter
    @Setter
    //이너클래스 생성 : 명시적으로 알아보기 쉬움., static으로 맞춰야함.
    public static class Request {
        @NotNull
        @Min(1)
        private Long userId;

        @NotNull
        @Min(100)
        private Long initialBalance;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor //빌더가 들어간 객체를 상속 받을 때 ALL,NoArgsConstructor 써야 문제 없음
    @Builder //빌더로 객체 생성
    public static class Response {
        private Long userId;    //유저아이디
        private String accountNumber;       //계좌번호
        private LocalDateTime registeredAt;     //등록일시

        public static Response from(AccountDto accountDto) {
            return Response.builder()
                    .userId(accountDto.getUserId())
                    .accountNumber(accountDto.getAccountNumber())
                    .registeredAt(accountDto.getRegisteredAt())
                    .build();
        }
    }
}
