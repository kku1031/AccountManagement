package com.example.account.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {
    //Account의 특정 정보들만 뽑아서 사용자한테 응답으로 줌
    //클라이언트와 Controller와의 응답을 주고 받는데 최적화
    private String accountNumber;
    private Long balance;




}
