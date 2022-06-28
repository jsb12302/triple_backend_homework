package com.example.triple_backend_homework.photos;

import com.example.triple_backend_homework.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(indexes = @Index(name = "i_photos", columnList = "photoId"))
public class Photos {

    @Id
    private String photoId;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID")
    Review review;

    public Photos(Review review, String photoId) {
        this.review = review;
        this.photoId = photoId;
    }

}
