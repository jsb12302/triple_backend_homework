package com.example.triple_backend_homework.reviewHistory;

import com.example.triple_backend_homework.account.AccountRepository;
import com.example.triple_backend_homework.point.PointService;
import com.example.triple_backend_homework.review.ReviewRepository;
import com.example.triple_backend_homework.review.requestDTO.RequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewHistoryService {

    private final PointService pointService;
    private final ReviewHistoryRepository reviewHistoryRepository;

    /**
     * 리뷰 이력 생성 기능
     * @param requestDTO
     */
    public void generateReviewHistory(RequestDTO requestDTO){
        int content = hasContent(requestDTO);
        int picture = hasPicture(requestDTO);
        int firstPost = hasFirstPost(requestDTO);

        ReviewHistory reviewHistory=new ReviewHistory();
        reviewHistory.setReviewId(requestDTO.getReviewId());
        reviewHistory.setContent(content);
        reviewHistory.setPicture(picture);
        reviewHistory.setFirstPost(firstPost);

        reviewHistoryRepository.save(reviewHistory);
    }

    /**
     * 리뷰 이력 수정 기능
     * @param requestDTO
     */
    public void updateReviewHistory(RequestDTO requestDTO){
        ReviewHistory byReview = reviewHistoryRepository.findByReviewId(requestDTO.getReviewId());

        int content = hasContent(requestDTO);
        int picture = hasPicture(requestDTO);
        int firstPost = hasFirstPost(requestDTO);

        byReview.setContent(content);
        byReview.setPicture(picture);
        byReview.setFirstPost(firstPost);

        reviewHistoryRepository.save(byReview);
    }


    /**
     * 글자수 1글자 이상 판단 기능
     * @param requestDTO
     * @return
     */
    public int hasContent(RequestDTO requestDTO){
        int points=0;
        int textSize = requestDTO.getContent().length();

        if(textSize>=1){
            points+=1;
        }

        return points;
    }

    /**
     * 그림 1개 이상 판단 기능
     * @param requestDTO
     * @return
     */
    public int hasPicture(RequestDTO requestDTO){
        int points=0;

        int photoSize = requestDTO.getAttachedPhotoIds().size();

        if(photoSize>=1){
            points+=1;
        }
        return points;
    }

    /**
     * 해당 지역 첫 리뷰 인지 아닌지 판단 기능
     * @param requestDTO
     * @return
     */
    public int hasFirstPost(RequestDTO requestDTO){
        return pointService.getBonusPoint(requestDTO);
    }


}
