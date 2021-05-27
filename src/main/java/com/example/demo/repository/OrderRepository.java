package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
	
	<T> T findByOrderId(UUID id, Class<T> type);
	Optional<Order> findByOrderNumber(String orderNumber);
}
