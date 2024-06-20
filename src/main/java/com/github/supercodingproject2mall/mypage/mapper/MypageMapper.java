package com.github.supercodingproject2mall.mypage.mapper;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.mypage.dto.MypageUserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MypageMapper {
    MypageMapper INSTANCE = Mappers.getMapper(MypageMapper.class);

    MypageUserInfo userEntityToMypage(UserEntity userEntity);
}
