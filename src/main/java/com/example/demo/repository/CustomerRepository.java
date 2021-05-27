package com.example.demo.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
	
	<T> T findByCustomerId(UUID id, Class<T> type);
}
