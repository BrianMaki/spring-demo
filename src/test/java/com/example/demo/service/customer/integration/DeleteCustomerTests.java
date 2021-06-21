package com.example.demo.service.customer.integration;

import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.model.Customer;
import com.example.demo.exception.EntityNotFoundException;

class DeleteCustomerTests extends Setup {

	@Test
	@Transactional
	void deleteCustomer_GivenValidCustomerId() {
		
		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		// act
		customerService.delete(customer.getCustomerId());
		
		// assert
		Assertions.assertTrue(customerRepository.findById(customer.getCustomerId()).isEmpty());
	}
	
	@Test
	@Transactional
	void deleteCustomer_GivenInvalidCustomerId_ThrowsEntityNotFoundException() {
		
		// arrange
		customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		UUID customerId = UUID.randomUUID();
		
		// act and assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> customerService.delete(customerId));
	}
}
