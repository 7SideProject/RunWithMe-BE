package com.runwithme.runwithme.domain.user.dto;

import org.hibernate.validator.constraints.Length;

public record UserChangePasswordDto(
        @Length(min = 8, max = 16, message = "비밀번호는 8 ~ 16자 이상이어야 합니다.")
        String password
) {
}
