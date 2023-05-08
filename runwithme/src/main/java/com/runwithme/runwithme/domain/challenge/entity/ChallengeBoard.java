package com.runwithme.runwithme.domain.challenge.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeBoard {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(name = "challenge_seq")
    private Long challengeSeq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "img_seq")
    private Long imgSeq;

    @Column(name = "challenge_board_content", length = 255)
    private String challengeBoardContent;

    @Column(name = "challenge_board_reg_time")
    private LocalDateTime challengeBoardRegTime;

    @Builder
    public ChallengeBoard(Long userSeq, Long challengeSeq, Long imgSeq, String challengeBoardContent, LocalDateTime challengeBoardRegTime){
        this.userSeq = userSeq;
        this.challengeSeq = challengeSeq;
        this.imgSeq = imgSeq;
        this.challengeBoardContent = challengeBoardContent;
        this.challengeBoardRegTime = challengeBoardRegTime;
    }
}
