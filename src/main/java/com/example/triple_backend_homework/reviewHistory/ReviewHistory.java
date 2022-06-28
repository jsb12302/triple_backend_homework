package com.example.triple_backend_homework.reviewHistory;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "i_reviewHistory", columnList = "id"))
public class ReviewHistory {

    @Id @GeneratedValue
    private Long id;

    private String reviewId;

    private int content;
    private int picture;
    private int firstPost;


}
