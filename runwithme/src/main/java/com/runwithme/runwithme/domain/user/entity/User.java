package com.runwithme.runwithme.domain.user.entity;

import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_seq")
    private Long seq;

    @Column(name = "user_role",
            nullable = false)
    private String role;

    @Column(name = "user_email",
            nullable = false,
            unique = true)
    private String email;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_height")
    private int height;

    @Column(name = "user_weight")
    private int weight;

    @Column(name = "user_point",
            nullable = false)
    private int point;

    @Builder
    public User(Long seq, String role, String email, String nickname, int height, int weight, int point) {
        this.seq = seq;
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.point = point;
    }

    public void setProfile(UserProfileDto dto) {
        this.nickname = dto.nickname();
        this.height = dto.height();
        this.weight = dto.weight();
        this.role = "ROLE_USER";
    }

    public boolean isTempUser() {
        return StringUtils.equals(role, "ROLE_TEMP_USER");
    }

    public static User create(OAuth2User oAuth2User) {
        return User.builder()
                .role("ROLE_TEMP_USER")
                .email(oAuth2User.getName())
                .point(0)
                .build();
    }
}
