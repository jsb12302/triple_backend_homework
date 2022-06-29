package com.example.triple_backend_homework.review;

import com.example.triple_backend_homework.account.Account;
import com.example.triple_backend_homework.account.AccountRepository;
import com.example.triple_backend_homework.point.PointService;
import com.example.triple_backend_homework.pointHistory.PointHistoryService;
import com.example.triple_backend_homework.review.requestDTO.RequestDTO;
import com.example.triple_backend_homework.reviewHistory.ReviewHistoryRepository;
import com.example.triple_backend_homework.reviewHistory.ReviewHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final PointService pointService;
    private final AccountRepository accountRepository;
    private final PointHistoryService pointHistoryService;
    private final ReviewHistoryService reviewHistoryService;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 작성 기능
     * @param requestDTO
     * @return
     */
    public boolean reviewAdd(RequestDTO requestDTO){
        Account account = accountRepository.findByUserId(requestDTO.getUserId());
        if(checkDuplicatedPlaceId(account, requestDTO.getPlaceId())) {
            int incrementPoints = pointService.incrementPoint(requestDTO);
            pointHistoryService.generatePointHistory(requestDTO,incrementPoints,"ADD","PLUS");
            reviewHistoryService.generateReviewHistory(requestDTO);
            return true;
        }
        return false;
    }

    /**
     * 사용자 방문 장소 중복 확인 기능
     * @param account
     * @param placeId
     */
    public boolean checkDuplicatedPlaceId(Account account, String placeId){
        Review byAccountAndPlaceId = reviewRepository.findByAccountAndPlaceId(account, placeId);
        if(Objects.isNull(byAccountAndPlaceId)){ //방문하지 않은 장소
            return true;
        }
        return false;
    }

}
