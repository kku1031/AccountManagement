package com.example.account.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity //AccountUser 엔티티(일종의 설정 클래스)
@EntityListeners(AuditingEntityListener.class)
public class AccountUser {
    //AccountUser 테이블에 pk를 id로 지정 하겠다.
    @Id
    @GeneratedValue
    private long id;

    //컬럼 추가
    private String name;

    //모든 테이블이 공통적으로 가지고 있으면 좋다.
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
