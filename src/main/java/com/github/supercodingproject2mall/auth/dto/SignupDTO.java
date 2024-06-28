package com.github.supercodingproject2mall.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class SignupDTO {

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일을 올바르게 입력하세요")
    private String email;

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "비밀번호는 영문자, 숫자의 조합으로 8자 이상 20자 이하로 설정해주세요")
    private String password;

    @NotBlank(message = "전화번호를 입력하세요")
    private String phoneNum;

    @NotBlank(message = "주소를 입력하세요")
    private String address;

    @NotBlank(message = "성별을 선택하세요")
    private String gender;
}
