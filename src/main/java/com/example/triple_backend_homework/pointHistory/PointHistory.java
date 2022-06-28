package com.example.triple_backend_homework.pointHistory;


import com.example.triple_backend_homework.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "i_pointHistory", columnList = "id"))
public class PointHistory {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account; //사용자 아이디

    private String reviewId; //리뷰 아이디
    private long amount; //포인트 량
    private int currentPoint; //현재 포인트
    private LocalDateTime time; //시간
    private String content; //변경 이유
    private String mode; //증감 여부


}
