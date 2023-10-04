package com.runwithme.runwithme.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record UserCreateDto(
        @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 아닙니다.")
        String email,
        @Length(min = 8, max = 16, message = "비밀번호는 8 ~ 16자 이상이어야 합니다.")
        String password,
        @Length(min = 2, max = 8, message = "닉네임은 2 ~ 8자이어야 합니다.")
        String nickname,
        @Positive(message = "양수를 입력해주세요.")
        int height,
        @Positive(message = "양수를 입력해주세요.")
        int weight
) {
}
