package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.dto.*;
import com.runwithme.runwithme.domain.user.dto.converter.UserConverter;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.AuthUtils;
import com.runwithme.runwithme.global.utils.ImageCache;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AuthUtils authUtils;

    public UserProfileViewDto join(UserCreateDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new CustomException(EMAIL_EXISTS);
        }

        User joinUser = UserConverter.toEntity(dto);
        return UserConverter.toViewDto(userRepository.save(joinUser));
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(DELETED_USER);
        return user;
    }

    public UserProfileViewDto setUserProfile(Long userSeq, UserProfileDto dto) {
        User user = findByUserSeq(userSeq);
        if (user.isDeleted()) throw new CustomException(DELETED_USER);
        if (!isCreatedUser(user)) throw new CustomException(NOT_RESOURCE_OWNER);
        user.setProfile(dto);
        return UserConverter.toViewDto(user);
    }

    public UserProfileViewDto getUserProfile(Long userSeq) {
        User user = findByUserSeq(userSeq);
        if (user.isDeleted()) throw new CustomException(DELETED_USER);
        return UserConverter.toViewDto(user);
    }

    public DuplicatedViewDto isDuplicatedEmail(String email) {
        return new DuplicatedViewDto(userRepository.existsByEmail(email));
    }

    public DuplicatedViewDto isDuplicatedNickname(String nickname) {
        return new DuplicatedViewDto(userRepository.existsByNickname(nickname));
    }

    public Resource getUserImage(Long userSeq) {
        User user = findByUserSeq(userSeq);
        if (user.isDeleted()) throw new CustomException(DELETED_USER);
        return imageService.getImage(user.getImage().getSeq());
    }

    public void changeImage(Long userSeq, UserProfileImageDto dto) {
        User user = findByUserSeq(userSeq);
        if (user.isDeleted()) throw new CustomException(DELETED_USER);
        if (!isCreatedUser(user)) throw new CustomException(NOT_RESOURCE_OWNER);
        if (!isDefaultImage(user)) {
            imageService.delete(user.getImage().getSeq());
        }
        user.changeImage(imageService.save(dto.image()));
    }

    private User findByUserSeq(Long userSeq) {
        User user = userRepository.findById(userSeq).orElseThrow(() -> new CustomException(SEQ_NOT_FOUND));
        if (user.isDeleted()) throw new CustomException(DELETED_USER);
        return user;
    }

    private boolean isDefaultImage(User user) {
        return ObjectUtils.nullSafeEquals(user.getImage().getSeq(), ImageCache.get(ImageCache.DEFAULT_PROFILE).getSeq());
    }

    private boolean isCreatedUser(User createdUser) {
        User loginUser = authUtils.getLoginUser();
        return Objects.equals(loginUser.getSeq(), createdUser.getSeq());
    }

    public void delete(Long userSeq) {
        User user = findByUserSeq(userSeq);
        if (!isCreatedUser(user)) throw new CustomException(NOT_RESOURCE_OWNER);
        user.delete();
    }

    public void changePassword(Long userSeq, UserChangePasswordDto dto) {
        User user = findByUserSeq(userSeq);
        if (!isCreatedUser(user)) throw new CustomException(NOT_RESOURCE_OWNER);
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.changePassword(encoder.encode(dto.password()));
    }
}
