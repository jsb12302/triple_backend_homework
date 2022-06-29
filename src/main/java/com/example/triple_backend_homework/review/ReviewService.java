package com.example.triple_backend_homework.review;

import com.example.triple_backend_homework.account.Account;
import com.example.triple_backend_homework.account.AccountRepository;
import com.example.triple_backend_homework.point.PointService;
import com.example.triple_backend_homework.pointHistory.PointHistoryService;
import com.example.triple_backend_homework.review.requestDTO.RequestDTO;
import com.example.triple_backend_homework.reviewHistory.ReviewHistory;
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
    private final ReviewHistoryRepository reviewHistoryRepository;

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
     * 리뷰 수정 시 placeId는 바뀌지 않는다는 가정 하
     * @param requestDTO
     * @return
     */
    public boolean reviewMod(RequestDTO requestDTO){
        if(hasReview(requestDTO)) {
            Account account = accountRepository.findByUserId(requestDTO.getUserId());
            ReviewHistory byReview = reviewHistoryRepository.findByReviewId(requestDTO.getReviewId());

            int historyContent = byReview.getContent();
            int historyFirstPost = byReview.getFirstPost();
            int historyPicture = byReview.getPicture();
            int historyTotalPoint = historyContent + historyFirstPost + historyPicture;

            int content = reviewHistoryService.hasContent(requestDTO);
            int firstPost = reviewHistoryService.hasFirstPost(requestDTO);
            int picture = reviewHistoryService.hasPicture(requestDTO);
            int totalPoint = content + firstPost + picture;

            if (totalPoint > historyTotalPoint) {
                int incrementPoint = totalPoint - historyTotalPoint;
                pointService.updatePoint(account, "PLUS", incrementPoint);
                pointHistoryService.generatePointHistory(requestDTO, incrementPoint, "MOD", "PLUS");
            }
            if (totalPoint < historyTotalPoint) {
                int decrementPoint = historyTotalPoint - totalPoint;
                pointService.updatePoint(account, "MINUS", decrementPoint);
                pointHistoryService.generatePointHistory(requestDTO, decrementPoint, "MOD", "MINUS");
            }
            reviewHistoryService.updateReviewHistory(requestDTO);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * 리뷰 삭제 기능
     * @param requestDTO
     * @return
     */
    public boolean reviewDelete(RequestDTO requestDTO){
        if(hasReview(requestDTO)){
            Review byPlaceId = reviewRepository.findByReviewId(requestDTO.getReviewId());
            int decrementPoints = pointService.decrementPoint(requestDTO);
            pointHistoryService.generatePointHistory(requestDTO,decrementPoints,"DELETE","MINUS");
            reviewRepository.delete(byPlaceId);
            reviewHistoryService.deleteReviewHistory(requestDTO);
            return true;
        }
        return false;

    }

    /**
     * 사용자 리뷰 존재 여부 판단 기능
     * @param requestDTO
     * @return
     */
    public boolean hasReview(RequestDTO requestDTO){
        Account account = accountRepository.findByUserId(requestDTO.getUserId());
        Review byAccountAndPlaceId = reviewRepository.findByAccountAndPlaceId(account, requestDTO.getPlaceId());
        if(Objects.isNull(byAccountAndPlaceId)){
            return false;
        }
        return true;
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
