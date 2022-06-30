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
public class DeleteReviewTest extends Thread{

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

    /**
     * 같은 장소에 첫 리뷰에 대한 A사용자 리뷰 삭제후 B사용자 리뷰 작성시
     * B사용자 에게 보너스 점수 부여한다.
     * @throws Exception
     */
    @Test
    @DisplayName("같은 장소에 대한 A사용자 리뷰 삭제후 B사용자 리뷰")
    public void removeAndFirstReview() throws Exception {

        String [] arr={"picture1","picture2"};

        HashMap request=new HashMap<>(); //첫번째 리뷰 작성 요청
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "좋아요");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr);

        HashMap request2=new HashMap<>(); //첫번째 리뷰 삭제 요청
        request2.put("type", "REVIEW");
        request2.put("action", "DELETE");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "좋아요");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr);

        HashMap request3=new HashMap<>(); //두번째 리뷰 생성 요청
        request3.put("type", "REVIEW");
        request3.put("action", "ADD");
        request3.put("reviewId", "reviewId2");
        request3.put("content", "좋아요");
        request3.put("userId", "userId2");
        request3.put("placeId", "placeId1");
        request3.put("attachedPhotoIds", arr);

        Account account=Account.builder() //첫번쨰 사용자 저장
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Account account2=Account.builder() //두번째 사용자 저장
                .userId(request3.get("userId").toString())
                .build();
        accountRepository.save(account2);

        Point point=Point.builder() //첫번째 사용자 포인트 초기화
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        Point point2=Point.builder() //두번째 사용자 포인트 초기화
                .point(0)
                .account(account2)
                .build();
        pointRepository.save(point2);

        mockMvc.perform(post("/events") //첫번째 리뷰 생성 요청
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

        mockMvc.perform(post("/events") //첫번째 리뷰 삭제 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        mockMvc.perform(post("/events") //두번째 리뷰 생성 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request3)))
                .andDo(print());

        Review review2=Review.builder() //두번째 리뷰 생성
                .reviewId(request3.get("reviewId").toString())
                .content(request3.get("content").toString())
                .placeId(request3.get("placeId").toString())
                .account(account2)
                .createTime(LocalDateTime.now())
                .build();
        reviewRepository.save(review2);

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(0);
        Assertions.assertThat(accountService.getAccountPoints(request3.get("userId").toString())).isEqualTo(3);
    }

    /**
     * 첫 리뷰, 텍스트 1자 이상, 사진 1자 이상 작성후 글 삭제
     * @throws Exception
     */
    @Test
    @DisplayName("첫리뷰, 글 작성, 사진x -> 글 삭제")
    public void removeFirstReview_Content() throws Exception {

        String [] arr={};

        HashMap request=new HashMap<>(); //첫번째 리뷰 작성 요청
        request.put("type", "REVIEW");
        request.put("action", "ADD");
        request.put("reviewId", "reviewId1");
        request.put("content", "좋아요");
        request.put("userId", "userId1");
        request.put("placeId", "placeId1");
        request.put("attachedPhotoIds", arr);

        HashMap request2=new HashMap<>(); //첫번째 리뷰 삭제 요청
        request2.put("type", "REVIEW");
        request2.put("action", "DELETE");
        request2.put("reviewId", "reviewId1");
        request2.put("content", "좋아요");
        request2.put("userId", "userId1");
        request2.put("placeId", "placeId1");
        request2.put("attachedPhotoIds", arr);

        Account account=Account.builder() //첫번쨰 사용자 저장
                .userId(request.get("userId").toString())
                .build();
        accountRepository.save(account);

        Point point=Point.builder() //첫번째 사용자 포인트 초기화
                .point(0)
                .account(account)
                .build();
        pointRepository.save(point);

        mockMvc.perform(post("/events") //첫번째 리뷰 생성 요청
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

        mockMvc.perform(post("/events") //첫번째 리뷰 삭제 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print());

        Assertions.assertThat(accountService.getAccountPoints(request.get("userId").toString())).isEqualTo(0);
    }

}













