package com.example.triple_backend_homework.review;

import com.example.triple_backend_homework.review.requestDTO.RequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/events")
    public ResponseEntity events(@RequestBody RequestDTO requestDTO){
        System.out.println("---------------------");
        System.out.println(requestDTO.toString());
        if(requestDTO.getAction().equals("ADD")){
            if(reviewService.reviewAdd(requestDTO)){
                return new ResponseEntity("리뷰가 작성 되었습니다.", HttpStatus.OK);
            }else{
                return new ResponseEntity("이미 같은 장소를 리뷰했습니다.", HttpStatus.BAD_REQUEST);
            }
        }
        if(requestDTO.getAction().equals("MOD")){
            if(reviewService.reviewMod(requestDTO)){
                return new ResponseEntity("리뷰가 수정 되었습니다.", HttpStatus.OK);
            }else{
                return new ResponseEntity("수정할 리뷰가 없습니다.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("올바르지 않은 요청입니다..", HttpStatus.BAD_REQUEST);
    }

}
