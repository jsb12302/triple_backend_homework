package com.example.triple_backend_homework.reviewTest;

import com.example.triple_backend_homework.account.Account;
import com.example.triple_backend_homework.account.AccountRepository;
import com.example.triple_backend_homework.point.Point;
import com.example.triple_backend_homework.point.PointRepository;
import com.example.triple_backend_homework.pointHistory.PointHistoryRepository;
import com.example.triple_backend_homework.review.Review;
import com.example.triple_backend_homework.review.ReviewRepository;
import com.example.triple_backend_homework.reviewHistory.ReviewHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ReviewTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ReviewHistoryRepository reviewHistoryRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void cleanUp(){
        reviewRepository.deleteAll();
        pointRepository.deleteAll();
        reviewHistoryRepository.deleteAll();
        pointHistoryRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("한 사용자 같은 장소 리뷰 중복")
    public void duplicatePlaceReview() throws Exception {

        String [] arr={"picture1","picture2"};

        HashMap request=new HashMap<>(); //첫번째 리뷰 생성 요청
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "좋아요");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr);

        HashMap request2=new HashMap<>(); //두번째 리뷰 생성 요청
        request2.put("type", "REVIEW");
        request2.put("action", "ADD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "좋아요");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr);

        Account account=Account.builder() //사용자 저장
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //포인트 초기화
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //첫 번째 리뷰 생성 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //리뷰 생성
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        mockMvc.perform(post("/events") //두번째 리뷰 생성 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("이미 같은 장소를 리뷰했습니다."))
                .andDo(print());

    }

}
