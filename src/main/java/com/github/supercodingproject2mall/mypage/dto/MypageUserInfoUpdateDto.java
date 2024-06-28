package com.github.supercodingproject2mall.mypage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MypageUserInfoUpdateDto {
    private String aboutMe;
    private String address;
    private MultipartFile profileImage;
}
