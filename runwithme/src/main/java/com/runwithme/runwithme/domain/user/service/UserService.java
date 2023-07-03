package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.dto.*;
import com.runwithme.runwithme.domain.user.dto.converter.UserConverter;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.CacheUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

import static com.runwithme.runwithme.global.error.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserProfileViewDto join(UserCreateDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        User joinUser = UserConverter.toEntity(dto);
        return UserConverter.toViewDto(userRepository.save(joinUser));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public UserProfileViewDto setUserProfile(Long userSeq, UserProfileDto dto) {
        User findUser = userRepository.findById(userSeq).orElseThrow(() ->
                new IllegalStateException("해당 SEQ를 가진 유저가 존재하지 않습니다.")
        );

        findUser.setProfile(dto);

        return UserConverter.toViewDto(findUser);
    }

    public UserProfileViewDto getUserProfile(Long userSeq) {
        User findUser = userRepository.findById(userSeq).orElseThrow(() ->
                new IllegalStateException("해당 SEQ를 가진 유저가 존재하지 않습니다.")
        );

        return UserConverter.toViewDto(findUser);
    }

    public DuplicatedViewDto isDuplicatedEmail(String email) {
        return new DuplicatedViewDto(userRepository.existsByEmail(email));
    }

    public DuplicatedViewDto isDuplicatedNickname(String nickname) {
        return new DuplicatedViewDto(userRepository.existsByNickname(nickname));
    }

    public Resource getUserImage(Long userSeq) {
        User user = userRepository.findById(userSeq).orElseThrow(() ->
                new IllegalStateException("해당 SEQ를 가진 유저가 존재하지 않습니다.")
        );
        return imageService.getImage(user.getImage().getSeq());
    }

    public void changeImage(Long userSeq, UserProfileImageDto dto) {
        try {
            User user = userRepository.findById(userSeq).orElseThrow(() ->
                    new IllegalStateException("해당 SEQ를 가진 유저가 존재하지 않습니다.")
            );
            if (!ObjectUtils.nullSafeEquals(user.getImage(), CacheUtils.get("defaultImage"))) {
                imageService.delete(user.getImage().getSeq());
            }
            user.changeImage(imageService.save(dto.image()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
