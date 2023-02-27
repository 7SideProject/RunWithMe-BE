package com.runwithme.runwithme.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_seq")
    private Long seq;

    // TODO : User Role 구현

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
    public User(Long seq, String email, String nickname, int height, int weight, int point) {
        this.seq = seq;
        this.email = email;
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.point = point;
    }
}
