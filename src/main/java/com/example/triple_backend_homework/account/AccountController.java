package com.example.triple_backend_homework.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * 사용자 계정으로 포인트 조회 기능
     * @param userId
     * @return userId, point json형식
     */
    @GetMapping("/events/{userId}")
    public ResponseEntity getPoints(@PathVariable String userId){
        String point = Integer.toString(accountService.getAccountPoints(userId));
        HashMap<String, String> points=new HashMap<>();
        points.put("userId",userId);
        points.put("point",point);
        return new ResponseEntity(points, HttpStatus.OK);
    }

}
