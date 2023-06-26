package com.example.account.domain;
import com.example.account.exception.AccountException;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor //빌더가 들어간 객체를 상속 받을 때 ALL,NoArgsConstructor 써야 문제 없음
@Builder //빌더로 객체 생성
@Entity //Account 엔티티(일종의 설정 클래스)
@EntityListeners(AuditingEntityListener.class) //config쪽에 전체 설정을 넣어주어야 어노테이션 작동
public class Account {
    //Account 테이블에 pk를 id로 지정하겠다
    @Id
    @GeneratedValue
    private Long id;

    //컬럼 추가
    @ManyToOne //유저를 N:1로 가짐. DB테이블에 user가 있어서 충돌날수도, AccountUser로 명시
    private AccountUser accountUser;
    private String accountNumber;

    @Enumerated(EnumType.STRING) //enum 값에 0,1,2,3을 저장하는게 아니라 실제 스트링 값 저장
    private AccountStatus accountStatus;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    //모든 테이블이 공통적으로 가지고 있으면 좋다, JPA에서 자동으로 저장해줌
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    //TransactionService에서 직접 금액을 조절하면 안정성의 위험이 있어서 이클래스에서 처리.
    public void useBalance(Long amount) {
        if (amount > balance) {
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
        balance -=amount;
    }


    public void cancelBalance(Long amount) {
        if (amount < 0) {
            throw new AccountException(ErrorCode.INVALID_REQUEST);
        }
        balance +=amount;
    }
}
