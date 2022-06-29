package com.example.triple_backend_homework.pointHistory;


import com.example.triple_backend_homework.account.Account;
import com.example.triple_backend_homework.account.AccountRepository;
import com.example.triple_backend_homework.account.AccountService;
import com.example.triple_backend_homework.review.requestDTO.RequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PointHistoryRepository pointHistoryRepository;

    /**
     * 포인트 변경 기록 저장 기능
     * @param requestDTO
     * @param amount
     * @param content
     * @param mode
     */
    public void generatePointHistory(RequestDTO requestDTO, int amount, String content, String mode){
        Account byUserId = accountRepository.findByUserId(requestDTO.getUserId());
        int accountPoints = accountService.getAccountPoints(requestDTO.getUserId());
        LocalDateTime now=LocalDateTime.now();

        PointHistory pointHistory=PointHistory.builder()
                .account(byUserId)
                .currentPoint(accountPoints)
                .time(now)
                .content(content)
                .mode(mode)
                .amount(amount)
                .reviewId(requestDTO.getReviewId())
                .build();
        pointHistoryRepository.save(pointHistory);
    }

}
