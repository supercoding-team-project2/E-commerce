package com.github.supercodingproject2mall.controller;

import com.github.supercodingproject2mall.mypage.dto.Mypage;
import com.github.supercodingproject2mall.mypage.dto.MypageResponse;
import com.github.supercodingproject2mall.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MypageController {
    private final MypageService mypageService;

    // 유저 정보 조회 api
    @GetMapping("/mypage/{userId}")
    public ResponseEntity<MypageResponse> findUserInfo(@PathVariable String userId){
        //TODO : jwt 구현시 userId 받아오는 로직 수정예정
        return mypageService.findUserInfo(userId)
                .map(mypage -> ResponseEntity.ok(new MypageResponse(Collections.singletonList(mypage))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
