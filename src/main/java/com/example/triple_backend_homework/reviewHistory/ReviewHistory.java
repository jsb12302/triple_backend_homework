package com.example.triple_backend_homework.reviewHistory;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "i_reviewId", columnList = "REVIEW_ID"))
public class ReviewHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REVIEW_ID")
    private String reviewId;

    private int content;
    private int picture;
    private int firstPost;


}
