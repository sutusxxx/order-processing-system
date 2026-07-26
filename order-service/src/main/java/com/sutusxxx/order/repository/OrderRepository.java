package com.sutusxxx.order.repository;

import com.sutusxxx.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
