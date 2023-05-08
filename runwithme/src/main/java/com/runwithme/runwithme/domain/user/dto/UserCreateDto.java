package com.runwithme.runwithme.domain.user.dto;

public record UserCreateDto(
        String email,
        String password,
        String nickname,
        int height,
        int weight
) {
}
