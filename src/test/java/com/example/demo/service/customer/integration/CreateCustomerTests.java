package com.example.demo.service.customer.integration;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.model.Customer;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.exception.UniqueConstraintException;

class CreateCustomerTests extends Setup {

	@Test
	@Transactional
	void createCustomer_GivenValidRequest_ReturnsCustomerResponse() {
		
		// arrange
		var request = CreateCustomerRequest.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build();
		
		// act
		CustomerResponse response = customerService.create(request);
		
		// assert
		Assertions.assertNotNull(response.getCustomerId());
		Assertions.assertEquals(FIRST_NAME_1, response.getFirstName());
		Assertions.assertEquals(LAST_NAME_1, response.getLastName());
	}
	
	@Test
	@Transactional
	void createCustomer_GivenInvalidRequest_ThrowUniqueConstraintException() {
		
		// arrange
		customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		var request = CreateCustomerRequest.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build();
		
		// act and assert
		Assertions.assertThrows(UniqueConstraintException.class, () -> customerService.create(request));
	}
}
