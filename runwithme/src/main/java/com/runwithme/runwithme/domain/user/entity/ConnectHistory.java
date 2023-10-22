package com.runwithme.runwithme.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConnectHistory {

    @Id
    @GeneratedValue
    @Column(name = "ch_seq")
    private Long seq;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(name = "ch_connect_datetime", nullable = false)
    private LocalDateTime connectDateTime;

    @Builder
    public ConnectHistory(User user, LocalDateTime connectDateTime) {
        this.user = user;
        this.connectDateTime = connectDateTime;
    }
}
