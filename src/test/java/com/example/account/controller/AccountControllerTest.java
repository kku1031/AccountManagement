package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.dto.AccountDto;
import com.example.account.dto.CreateAccount;
import com.example.account.dto.DeleteAccount;
import com.example.account.service.AccountService;
import com.example.account.type.AccountStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//서비스가 AccountController 에 자동으로 주입됨, 자체적으로 MockMvc를 만들어줌.
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @MockBean //스프링 자체내에서 목테스트, 모키토랑 다름.
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired  //잭슨, 상호변환 시켜주는 오브젝트 매퍼
    private ObjectMapper objectMapper;

    //성공case
    @Test
        void successCreateAccount () throws Exception {
        //given 어떤 데이터가 있을 때,
        given(accountService.createAccount(anyLong(), anyLong()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAt(LocalDateTime.now())
                        .unRegisteredAt(LocalDateTime.now())
                        .build());
        //when 어떤 동작을 하게 되면
        //then 어떤 결과가 나와야한다
        mockMvc.perform(post("/account")
                //커뮤니티 버전에서는 http 사용할수 없어서 살짝 수정
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(
                        new CreateAccount.Request(3333L,1111L)
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountNumber").value(1234567890))
                .andDo(print());
    }
    @Test
    void successGetAccountsByUserId() throws Exception {
        //given 어떤 데이터가 있을 때,
        List<AccountDto> accountDtos =
                Arrays.asList(
                        AccountDto.builder()
                                .accountNumber("1234567890")
                                .balance(1000L).build(),
                        AccountDto.builder()
                                .accountNumber("1111111111")
                                .balance(2000L).build(),
                        AccountDto.builder()
                                .accountNumber("2222222222")
                                .balance(3000L).build()
                );
        given(accountService.getAccountsByUserId(anyLong()))
                .willReturn(accountDtos);
        //then
        mockMvc.perform(get("/account?user_id=1"))
                .andDo(print())
                .andExpect(jsonPath("$[0].accountNumber").value("1234567890"))
                .andExpect(jsonPath("$[0].balance").value("1000"))
                .andExpect(jsonPath("$[1].accountNumber").value("1111111111"))
                .andExpect(jsonPath("$[1].balance").value("2000"))
                .andExpect(jsonPath("$[2].accountNumber").value("2222222222"))
                .andExpect(jsonPath("$[2].balance").value("3000"));
    }

    @Test
    void successGetAccount() throws Exception {
        //given
        given(accountService.getAccount(anyLong()))
                .willReturn(Account.builder()
                        .accountNumber("3456")
                        .accountStatus(AccountStatus.IN_USE)
                        .build());
        //when : 결과가 http 응답형식으로 와서 바로 결과.

        //then
        mockMvc.perform(get("/account/876"))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber").value("3456"))
                .andExpect(jsonPath("$.accountStatus").value("IN_USE"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("삭제성공테스트")
    void successDeleteAccount () throws Exception {
        //given 어떤 데이터가 있을 때,
        given(accountService.deleteAccount(anyLong(), anyString()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAt(LocalDateTime.now())
                        .unRegisteredAt(LocalDateTime.now())
                        .build());
        //then 어떤 결과가 나와야한다
        mockMvc.perform(delete("/account")
                        //커뮤니티 버전에서는 http 사용할수 없어서 살짝 수정
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(
                                new DeleteAccount.Request(3333L,"0987654321")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andDo(print());
    }

//    @Test
//    void failGetAccount() throws Exception {
//        //given
//        given(accountService.getAccount(anyLong()))
//                .willThrow(new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
//        //then
//        mockMvc.perform(get("/account/876"))
//                .andDo(print())
//                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUND"))
//                .andExpect(jsonPath("$.errorMessage").value("계좌가 없습니다."))
//                .andExpect(status().isOk());
//    }
}