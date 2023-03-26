package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.dto.converter.UserConverter;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileViewDto setUserProfile(Long userSeq, UserProfileDto dto) {
        User findUser = userRepository.findById(userSeq).orElseThrow(() -> new IllegalArgumentException("Not found user by invalid user sequence."));

        findUser.setProfile(dto);

        return UserConverter.toViewDto(findUser);
    }
}
