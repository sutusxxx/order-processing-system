package com.sutusxxx.payment.repository;

import com.sutusxxx.payment.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedRepository extends JpaRepository<ProcessedEvent, Long> {
    boolean existsByMessageId(String messageId);
}
