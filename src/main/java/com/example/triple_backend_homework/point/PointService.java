package com.example.triple_backend_homework.point;

import com.example.triple_backend_homework.account.Account;
import com.example.triple_backend_homework.account.AccountRepository;
import com.example.triple_backend_homework.review.Review;
import com.example.triple_backend_homework.review.ReviewRepository;
import com.example.triple_backend_homework.review.requestDTO.RequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final ReviewRepository reviewRepository;
    private final PointRepository pointRepository;
    private final AccountRepository accountRepository;

    /**
     * 포인트 증가 기능
     * @param requestDTO
     * @return
     */
    public int incrementPoint(RequestDTO requestDTO){
        Account findAccountByUserId = accountRepository.findByUserId(requestDTO.getUserId());
        Point findPointByAccount = pointRepository.findByAccount(findAccountByUserId);

        int originPoint = findPointByAccount.getPoint();
        int contentPoint = getContentPoint(requestDTO);
        int bonusPoint = getBonusPoint(requestDTO);

        int incrementPoint = contentPoint + bonusPoint;

        findPointByAccount.setPoint(originPoint + contentPoint + bonusPoint);
        pointRepository.save(findPointByAccount);
        return incrementPoint;
    }

    /**
     * 내용 점수 기능
     * @param requestDTO
     * @return 내용 점수
     */
    public int getContentPoint(RequestDTO requestDTO){
        int points=0;
        int textSize = requestDTO.getContent().length();
        int photoSize = requestDTO.getAttachedPhotoIds().size();
        if(textSize>=1){
            points+=1;
        }
        if(photoSize>=1){
            points+=1;
        }
        return points;
    }

    /**
     * 보너스 포인트 점수 기능
     * @param requestDTO
     * @return 보너스 점수
     */
    public int getBonusPoint(RequestDTO requestDTO){
        int point=0;
        List<Review> byPlaceId = reviewRepository.findByPlaceId(requestDTO.getPlaceId());
        if(byPlaceId.size()<1) {
            point += 1;
        }
        if(byPlaceId.size()>=1) { //같은 사용자가 같은 장소로 리뷰 작성해도 장소 중복 검사 통해 예외 해결
            if (byPlaceId.get(0).getAccount().getUserId().equals(requestDTO.getUserId())) {
                //지울 때 첫 리뷰 인지 판단
                point += 1;
            }
        }
        return point;
    }

}
