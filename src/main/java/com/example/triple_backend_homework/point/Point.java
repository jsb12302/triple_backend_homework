package com.example.triple_backend_homework.point;

import com.example.triple_backend_homework.account.Account;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "i_point", columnList = "id"))
public class Point {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    private int point;

}
