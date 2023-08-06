package com.runwithme.runwithme.global.utils;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.exception.EntityNotFoundException;
import com.runwithme.runwithme.global.error.exception.UserNotLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.runwithme.runwithme.global.error.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public User getLoginUser() {
        final String userEmail = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        final User loginUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return loginUser;
    }

    public Long getLoginUserSeq() {
        final String userEmail = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        final User loginUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return loginUser.getSeq();
    }
}
