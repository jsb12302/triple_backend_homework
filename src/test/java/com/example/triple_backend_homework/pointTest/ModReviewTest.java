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
    @DisplayName("사용자 첫 리뷰, 글 존재, 사진x : 2포인트 -> 첫리뷰, 글존재, 사진존재 : 3포인트")
    public void onlyFistContent()throws Exception {
        String [] arr={};
        String [] arr2={"picture1","picture2"};

        HashMap request=new HashMap<>(); //첫번째 리뷰 생성 요청
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "좋아요");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr); //사진 존재하지 않음

        HashMap request2=new HashMap<>(); //첫번째 리뷰 수정 요청
        request2.put("type", "REVIEW");
        request2.put("action", "MOD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "좋아요");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr2); //사진 존재

        Account account=Account.builder() //사용자 저장
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //포인트 초기화
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //첫번째 리뷰 생성 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //첫번째 리뷰 생성
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(2);

        mockMvc.perform(post("/events") //첫번째 리뷰 수정 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(3);
    }

    @Test
    @DisplayName("사용자 첫 리뷰, 글 존재, 사진 : 3포인트 -> 첫리뷰, 글존재, 사진x : 2포인트")
    public void firstContent_Photo() throws Exception{

        String [] arr={"picture1","picture2"};
        String [] arr2={};

        HashMap request=new HashMap<>(); //첫번째 리뷰 생성 요청
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "좋아요");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr); //사진 존재

        HashMap request2=new HashMap<>(); //첫번째 리뷰 수정 요청
        request2.put("type", "REVIEW");
        request2.put("action", "MOD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "좋아요");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr2); //사진 존재하지 않음

        Account account=Account.builder() //사용자 저장
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //포인트 초기화
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //첫번쨰 리뷰 생성 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //첫번째 리뷰 생성
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(3);

        mockMvc.perform(post("/events") //첫번째 리뷰 수정 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(2);

    }

    @Test
    @DisplayName("사용자 첫 리뷰, 글 존재, 사진 : 3포인트 -> 첫리뷰, 글x, 사진x : 1포인트")
    public void noContent_Photo() throws Exception{

        String [] arr={"picture1","picture2"};
        String [] arr2={};

        HashMap request=new HashMap<>(); //첫번째 리뷰 생성 요청
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "좋아요");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr); //사진 존재

        HashMap request2=new HashMap<>(); //첫번째 리뷰 수정 요청
        request2.put("type", "REVIEW");
        request2.put("action", "MOD");
        request2.put("reviewId", "reviewId1");
        request2.put("content", ""); //글 존재하지 않음
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr2); //사진 존재하지 않음

        Account account=Account.builder() //사용자 저장
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //포인트 초기화
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //첫번째 리뷰 작성 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        Review review=Review.builder() //첫번쨰 리뷰 생성
                .reviewId(request.get("reviewId").toString())
                .content(request.get("content").toString())
                .placeId(request.get("placeId").toString())
                .account(account)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(3);

        mockMvc.perform(post("/events") //첫번쨰 리뷰 수정 요청 보내기
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(1);

    }

}
