package com.runwithme.runwithme.domain.challenge.entity;

import com.runwithme.runwithme.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "challenge_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_user_seq", columnDefinition = "INT UNSIGNED")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", columnDefinition = "UNSIGNED")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_seq", columnDefinition = "INT UNSIGNED")
    private Challenge challenge;

    @Builder
    public ChallengeUser(User user, Challenge challenge){
        this.user = user;
        this.challenge = challenge;
    }
}
