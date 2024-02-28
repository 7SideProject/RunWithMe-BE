package com.runwithme.runwithme.global.utils;

import static com.runwithme.runwithme.global.result.ResultCode.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.CustomException;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class AuthUtils {

	private final UserRepository userRepository;

	public User getLoginUser() {
		final String userSeq = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
		return userRepository.findById(Long.valueOf(userSeq))
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));
	}

	public Long getLoginUserSeq() {
		final String userSeq = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
		return Long.valueOf(userSeq);
	}
}
