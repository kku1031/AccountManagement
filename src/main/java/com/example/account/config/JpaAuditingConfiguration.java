package com.example.account.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
    //스프링 애플리케이션 클래스를 시작할때 같이 동작.(DB에 데이터 저장할때, 어노테이션 붙은 부분들 같이 동작)
}
