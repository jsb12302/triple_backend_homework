package com.example.triple_backend_homework.pointTest;

import com.example.triple_backend_homework.account.Account;
import com.example.triple_backend_homework.account.AccountRepository;
import com.example.triple_backend_homework.account.AccountService;
import com.example.triple_backend_homework.point.Point;
import com.example.triple_backend_homework.point.PointRepository;
import com.example.triple_backend_homework.pointHistory.PointHistoryRepository;
import com.example.triple_backend_homework.review.Review;
import com.example.triple_backend_homework.review.ReviewRepository;
import com.example.triple_backend_homework.reviewHistory.ReviewHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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

@SpringBootTest
public class ModReviewTest {

    @Autowired
    private AccountService accountService;
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
    @DisplayName("????????? ??? ??????, ??? ??????, ??????x : 2????????? -> ?????????, ?????????, ???????????? : 3?????????")
    public void onlyFistContent()throws Exception {
        String [] arr={};
        String [] arr2={"picture1","picture2"};

        HashMap request=new HashMap<>(); //????????? ?????? ?????? ??????
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "?????????");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr); //?????? ???????????? ??????

        HashMap request2=new HashMap<>(); //????????? ?????? ?????? ??????
        request2.put("type", "REVIEW");
        request2.put("action", "MOD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "?????????");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr2); //?????? ??????

        Account account=Account.builder() //????????? ??????
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //????????? ?????????
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //????????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //????????? ?????? ??????
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(2);

        mockMvc.perform(post("/events") //????????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(3);
    }

    @Test
    @DisplayName("????????? ??? ??????, ??? ??????, ?????? : 3????????? -> ?????????, ?????????, ??????x : 2?????????")
    public void firstContent_Photo() throws Exception{

        String [] arr={"picture1","picture2"};
        String [] arr2={};

        HashMap request=new HashMap<>(); //????????? ?????? ?????? ??????
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "?????????");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr); //?????? ??????

        HashMap request2=new HashMap<>(); //????????? ?????? ?????? ??????
        request2.put("type", "REVIEW");
        request2.put("action", "MOD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "?????????");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr2); //?????? ???????????? ??????

        Account account=Account.builder() //????????? ??????
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //????????? ?????????
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //????????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //????????? ?????? ??????
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(3);

        mockMvc.perform(post("/events") //????????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(2);

    }

    @Test
    @DisplayName("????????? ??? ??????, ??? ??????, ?????? : 3????????? -> ?????????, ???x, ??????x : 1?????????")
    public void noContent_Photo() throws Exception{

        String [] arr={"picture1","picture2"};
        String [] arr2={};

        HashMap request=new HashMap<>(); //????????? ?????? ?????? ??????
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "?????????");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr); //?????? ??????

        HashMap request2=new HashMap<>(); //????????? ?????? ?????? ??????
        request2.put("type", "REVIEW");
        request2.put("action", "MOD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", ""); //??? ???????????? ??????
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr2); //?????? ???????????? ??????

        Account account=Account.builder() //????????? ??????
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //????????? ?????????
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //????????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //????????? ?????? ??????
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(3);

        mockMvc.perform(post("/events") //????????? ?????? ?????? ?????? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(1);

    }

}
