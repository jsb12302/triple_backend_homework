package com.example.triple_backend_homework.review;

import com.example.triple_backend_homework.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,String> {

    @Query("SELECT r from Review as r where r.account = :account and r.placeId= :placeId")
    Review findByAccountAndPlaceId(@Param("account") Account account,
                                   @Param("placeId")String placeId);

    @Query("select r from Review as r where r.placeId = :placeId order by r.createTime asc")
    List<Review> findByPlaceId(@Param("placeId")String placeId);

    Review findByReviewId(String reviewId);

}
