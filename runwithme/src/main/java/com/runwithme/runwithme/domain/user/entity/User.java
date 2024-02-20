package com.runwithme.runwithme.domain.user.entity;

import java.time.LocalDateTime;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.runwithme.runwithme.domain.user.dto.UserInfoResponse;
import com.runwithme.runwithme.domain.user.dto.UserProfileDto;
import com.runwithme.runwithme.global.entity.BaseEntity;
import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.utils.ImageCache;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_seq")
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_seq")
    private Image image;

    @Column(name = "user_role",
            nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "user_email",
            nullable = false,
            unique = true)
    private String email;

    @Column(name = "user_password",
            nullable = false,
            unique = true)
    private String password;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "user_height")
    private int height;

    @Column(name = "user_weight")
    private int weight;

    @Column(name = "user_point",
            nullable = false)
    private int point;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private ProviderType providerType;

    @Column(name = "resource_id")
    private String resourceId;

    @Builder
    public User(
        Long seq,
        Image image,
        Role role,
        String email,
        String nickname,
        String password,
        int height,
        int weight,
        int point,
        ProviderType providerType,
        String resourceId
    ) {
        this.seq = seq;
        this.image = image;
        this.role = role;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.point = point;
        this.providerType = providerType;
        this.resourceId = resourceId;
    }

    public String getRoleValue() {
        return role.getValue();
    }

    public int getRoleStatus() {
        return role.getStatus();
    }


    public void setProfile(UserProfileDto dto) {
        this.nickname = dto.nickname();
        this.height = dto.height();
        this.weight = dto.weight();
        this.role = Role.USER;
    }

    public boolean isInitialized() {
        return role != Role.TEMP_USER;
    }

    public void changeImage(Image image) {
        this.image = image;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void subtractPoint(int point) {
        this.point -= point;
    }

    public static User create(ProviderType providerType, String resourceId) {
        return User.builder()
                .role(Role.TEMP_USER)
                .providerType(providerType)
                .resourceId(resourceId)
                .point(1500)
                .image(ImageCache.get(ImageCache.DEFAULT_PROFILE))
                .build();
    }

	public void changePassword(String password) {
		this.password = password;
	}

	public void plusPoint(int point) {
		this.point += point;
	}

	public void minusPoint(int point) {
		this.point -= point;
	}
}
