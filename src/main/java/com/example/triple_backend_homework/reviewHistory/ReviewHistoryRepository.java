package com.example.triple_backend_homework.reviewHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHistoryRepository extends JpaRepository<ReviewHistory,Long> {

    ReviewHistory findByReviewId(String reviewId);

}
