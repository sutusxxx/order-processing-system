package com.sutusxxx.order.repository;

import com.sutusxxx.order.OutboxEvent;
import com.sutusxxx.order.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop100ByStatusOrderByCreatedAt(OutboxStatus status);
}
