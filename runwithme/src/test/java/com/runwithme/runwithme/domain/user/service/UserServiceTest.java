package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.MockEntityFactory;
import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.domain.user.dto.UserProfileViewDto;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@DisplayName("음원 추가 서비스 단위 테스트")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("[프로필 수정] 사용자는 유저 프로필(닉네임, 키, 몸무게)를 수정할 수 있습니다.")
    class SetUserProfile {

        UserProfileDto givenDto;
        UserProfileViewDto resultDto;

        @BeforeEach
        void beforeEach() {
            givenDto = new UserProfileDto("명범", 173, 60);
            resultDto = UserProfileViewDto.builder()
                    .seq(1L)
                    .email("mungmnb777@gmail.com")
                    .height(173)
                    .weight(60)
                    .nickname("명범")
                    .point(0)
                    .build();
        }

        @Test
        @DisplayName("[성공 케이스]")
        void success() {
            // given
            BDDMockito.given(userRepository.findById(eq(1L))).willReturn(Optional.of(MockEntityFactory.user()));

            // when
            UserProfileViewDto actual = userService.setUserProfile(1L, givenDto);

            // then
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(resultDto);

        }

        @Test
        @DisplayName("[실패 케이스 1] 해당 SEQ의 유저가 없는 경우")
        void failure() {
            // given
            BDDMockito.given(userRepository.findById(eq(1L))).willThrow(IllegalArgumentException.class);

            // when

            // then
            Assertions.assertThatThrownBy(() -> userService.setUserProfile(1L, givenDto)).isInstanceOf(IllegalArgumentException.class);
        }
    }


}