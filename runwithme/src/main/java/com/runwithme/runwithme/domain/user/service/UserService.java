package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.dto.*;
import com.runwithme.runwithme.domain.user.dto.converter.UserConverter;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.service.ImageService;
import com.runwithme.runwithme.global.utils.CacheUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserProfileViewDto join(UserCreateDto dto) {
        if (userRepository.existsByEmail(dto.email())) throw new IllegalArgumentException("이미 존재하는 이메일입니다.");

        User joinUser = UserConverter.toEntity(dto);

        return UserConverter.toViewDto(userRepository.save(joinUser));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
    }

    public UserProfileViewDto setUserProfile(Long userSeq, UserProfileDto dto) {
        User findUser = userRepository.findById(userSeq).orElseThrow(() -> new IllegalArgumentException("Not found user by invalid user sequence."));

        findUser.setProfile(dto);

        return UserConverter.toViewDto(findUser);
    }

    public UserProfileViewDto getUserProfile(Long userSeq) {
        User findUser = userRepository.findById(userSeq).orElseThrow(() -> new IllegalArgumentException("Not found user by invalid user sequence."));

        return UserConverter.toViewDto(findUser);
    }

    public DuplicatedViewDto isDuplicatedEmail(String email) {
         return new DuplicatedViewDto(userRepository.existsByEmail(email));
    }

    public DuplicatedViewDto isDuplicatedNickname(String nickname) {
        return new DuplicatedViewDto(userRepository.existsByNickname(nickname));
    }

    public Resource getUserImage(Long userSeq) {
        User user = userRepository.findById(userSeq).orElseThrow(IllegalArgumentException::new);
        return imageService.getImage(user.getImage().getSeq());
    }

    public void changeImage(Long userSeq, UserProfileImageDto dto) throws IOException {
        User user = userRepository.findById(userSeq).orElseThrow(IllegalArgumentException::new);

        if (!ObjectUtils.nullSafeEquals(user.getImage(), CacheUtils.get("defaultImage"))) imageService.delete(user.getImage().getSeq());

        Image savedImage = imageService.save(dto.image());

        user.changeImage(savedImage);
    }
}
