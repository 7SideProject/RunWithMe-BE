package com.runwithme.runwithme.domain.user.dto.converter;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.runwithme.runwithme.domain.user.dto.UserCreateDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.entity.Role;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.utils.ImageCache;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.runwithme.runwithme.global.utils.CacheUtils;

public class UserConverter {

    private static final int DEFAULT_USER_POINT = 1500;

    public static User toEntity(UserCreateDto dto) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        Image defaultImage = ImageCache.get(ImageCache.DEFAULT_PROFILE);

        return User.builder()
                .image(defaultImage)
                .email(dto.email())
                .password(encoder.encode(dto.password()))
                .nickname(dto.nickname())
                .weight(dto.weight())
                .height(dto.height())
                .role(Role.USER)
                .point(DEFAULT_USER_POINT)
                .build();
    }

    public static UserProfileViewDto toViewDto(User user) {
        return UserProfileViewDto.builder()
                .seq(user.getSeq())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .height(user.getHeight())
                .weight(user.getWeight())
                .point(user.getPoint())
                .build();
    }
}
