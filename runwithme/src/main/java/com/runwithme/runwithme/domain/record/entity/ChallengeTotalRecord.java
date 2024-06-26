package com.runwithme.runwithme.domain.record.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ChallengeTotalRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private Long seq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "challenge_seq")
    private Long challengeSeq;

    @Column(name = "total_time")
    @ColumnDefault("0")
    private Long totalTime;

    @Column(name = "total_distance")
    @ColumnDefault("0")
    private Long totalDistance;

    @Column(name = "total_calorie")
    @ColumnDefault("0")
    private Long totalCalorie;

    @Column(name = "total_longest_time")
    @ColumnDefault("0")
    private Long totalLongestTime;

    @Column(name = "total_longest_distance")
    @ColumnDefault("0")
    private Long totalLongestDistance;

    @Column(name = "total_avg_speed")
    @ColumnDefault("0")
    private double totalAvgSpeed;

    @Builder
    public ChallengeTotalRecord(Long userSeq, Long challengeSeq) {
        this.userSeq = userSeq;
        this.challengeSeq = challengeSeq;
    }

    public void plusTotalTime(Long totalTime) {
        this.totalTime += totalTime;
    }
    public void plusTotalDistance(Long totalDistance) {
        this.totalDistance += totalDistance;
    }
    public void plusTotalCalorie(Long totalCalorie) { this.totalCalorie += totalCalorie; }
    public void setTotalAvgSpeed(double totalAvgSpeed) { this.totalAvgSpeed = totalAvgSpeed; }
    public void setTotalLongestTime(Long totalLongestTime) { this.totalLongestTime = totalLongestTime; }
    public void setTotalLongestDistance(Long totalLongestDistance) { this.totalLongestDistance = totalLongestDistance; }
}
