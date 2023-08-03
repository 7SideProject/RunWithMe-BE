package com.runwithme.runwithme.global.utils;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.runwithme.runwithme.global.result.ResultCode.USER_NOT_FOUND;


@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public User getLoginUser() {
        final Long userSeq = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userSeq)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public Long getLoginUserSeq() {
        try {
            return (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new CustomException(USER_NOT_FOUND);
        }
    }
}
