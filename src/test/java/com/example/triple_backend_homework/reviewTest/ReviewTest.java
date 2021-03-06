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
    @DisplayName("??? ????????? ?????? ?????? ?????? ??????")
    public void duplicatePlaceReview() throws Exception {

        String [] arr={"picture1","picture2"};

        HashMap request=new HashMap<>(); //????????? ?????? ?????? ??????
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "?????????");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr);

        HashMap request2=new HashMap<>(); //????????? ?????? ?????? ??????
        request2.put("type", "REVIEW");
        request2.put("action", "ADD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "?????????");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr);

        Account account=Account.builder() //????????? ??????
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //????????? ?????????
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //??? ?????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //?????? ??????
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        mockMvc.perform(post("/events") //????????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("?????? ?????? ????????? ??????????????????."))
                .andDo(print());

    }

}
