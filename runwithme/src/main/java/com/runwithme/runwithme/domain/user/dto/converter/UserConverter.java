package com.runwithme.runwithme.domain.user.dto.converter;

import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.entity.User;

public class UserConverter {

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
