package com.github.supercodingproject2mall.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MypageResponse {
    private List<MypageUserInfo> mypageUserInfos;
}
