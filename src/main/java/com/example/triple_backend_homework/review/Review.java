package com.example.triple_backend_homework.review;

import com.example.triple_backend_homework.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "i_placeId", columnList = "PLACE_ID"))
public class Review {

    @Id
    @Column(name = "REVIEW_ID")
    private String reviewId;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Account account;

    @Column(name = "PLACE_ID")
    private String placeId;
    private String content;
    private LocalDateTime createTime;

}
