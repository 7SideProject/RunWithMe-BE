package com.runwithme.runwithme.domain.user.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.runwithme.runwithme.domain.user.entity.ConnectHistory;

public interface ConnectHistoryRepository extends JpaRepository<ConnectHistory, Long> {
	boolean existsByConnectDateTimeIsAfterAndUserSeq(LocalDateTime dateTime, Long userSeq);
}
