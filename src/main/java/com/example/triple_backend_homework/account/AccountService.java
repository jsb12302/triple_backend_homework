package com.example.triple_backend_homework.account;


import com.example.triple_backend_homework.point.Point;
import com.example.triple_backend_homework.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PointRepository pointRepository;

    /**
     * 계정 포인트 조회 기능
     * @param userId
     * @return
     */
    public int getAccountPoints(String userId){
        Account byUserId = accountRepository.findByUserId(userId);
        Point byAccount = pointRepository.findByAccount(byUserId);
        return byAccount.getPoint();
    }

}
