package com.example.account.exception;


import com.example.account.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//비즈니스 로직에 딱맞는 Exception이 있는 경우가 잘 없어서
//보통 customException으로 별도의 exception패키지를 만들어서 관리.

public class AccountException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public AccountException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    @Override
    public String getMessage() {
        return errorMessage; // 예외 메시지로 errorMessage 반환
    }
}
