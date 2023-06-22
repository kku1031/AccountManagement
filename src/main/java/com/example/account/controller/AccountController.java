package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.dto.CreateAccount;
import com.example.account.service.AccountService;
import com.example.account.service.RedisTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController //@RestController는 @Controller에 @ResponseBody가 추가된 것입니다. 당연하게도 RestController의 주용도는 Json 형태로 객체 데이터를 반환
@RequiredArgsConstructor
public class AccountController {
    //레이어드 아키텍처 구조 : 외부 -> controller -> Service -> Repository로 순차적으로 접속하는 구조.
    //의존성 주입 받는 부분에 final을 넣지 않아 오류가 발생했었음.
    private final AccountService accountService; //의존성 주입 받은 부분

    private final RedisTestService redisTestService;

    @GetMapping("/get-lock")
    public String getLock() {
        return redisTestService.getLock();
    }

    @PostMapping("/account")
//  @RequestBody는 클라이언트가 전송하는 Json(application/json) 형태의 HTTP Body 내용을 Java Object로 변환시켜주는 역할
//  CreateAccount의 Request객체 -> 요청 : RequestBody -> 그 값중 getUserId,getInitialBalance를 통해 들어옴.
    public CreateAccount.Response createAccount(
            @RequestBody @Valid CreateAccount.Request request
    ) {
        return CreateAccount.Response.from(
                accountService.createAccount(
                        request.getUserId(),
                        request.getInitialBalance()
                )
        );
    }

    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id) {
        return accountService.getAccount(id);
    }
}
