package com.example.demo.service.customer.integration;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.model.Customer;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.exception.EntityNotFoundException;

class GetCustomerTests extends Setup {

	@Test
	@Transactional
	void getCustomer_ReturnsCustomerResponseList() {
		
		int expectedSize = 2;
		
		// arrange
		customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_2)
				.lastName(LAST_NAME_2)
				.build());
		
		// act
		List<CustomerResponse> list = customerService.get();
		
		// assert
		Assertions.assertEquals(expectedSize, list.size());
	}
	
	@Test
	@Transactional
	void getCustomer_ReturnsEmptyCustomerResponseList() {
		
		int expectedSize = 0;
		
		// arrange
		
		// act
		List<CustomerResponse> list = customerService.get();
		
		// assert
		Assertions.assertEquals(expectedSize, list.size());
	}
	
	@Test
	@Transactional
	void getCustomerById_GivenValidId_ReturnsCustomerView() {
		
		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		// act
		var view = customerService.get(customer.getCustomerId());
		
		// assert
		Assertions.assertNotNull(view);
		Assertions.assertEquals(NAME_1, view.getName());
	}
	
	@Test
	@Transactional
	void getCustomerById_GivenInvalidId_ThrowsEntityNotFoundException() {
		
		// arrange
		UUID customerId = UUID.randomUUID();
		
		
		// act and assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> customerService.get(customerId));
	}
}
