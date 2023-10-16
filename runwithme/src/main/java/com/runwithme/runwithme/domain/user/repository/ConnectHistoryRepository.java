package com.runwithme.runwithme.domain.user.repository;

import com.runwithme.runwithme.domain.user.entity.ConnectHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConnectHistoryRepository extends JpaRepository<ConnectHistory, Long> {

    boolean existsByConnectDateTimeIsAfterAndUserSeq(LocalDateTime dateTime, Long userSeq);
}
