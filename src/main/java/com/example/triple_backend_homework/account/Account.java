package com.example.triple_backend_homework.account;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "i_account", columnList = "ACCOUNT_ID"))
public class Account {

    @Id
    @Column(name = "ACCOUNT_ID")
    private String userId;

}
