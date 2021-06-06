package com.example.demo.service.customer.integration;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.model.Customer;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.dto.UpdateCustomerRequest;
import com.example.demo.exception.OptimisticLockException;
import com.example.demo.exception.UniqueConstraintException;

class UpdateCustomerTests extends Setup {

	@Test
	@Transactional
	void updateCustomer_GivenValidRequest_ReturnsCustomerResponse() {
		
		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		var request = UpdateCustomerRequest.builder()
				.customerId(customer.getCustomerId())
				.firstName(FIRST_NAME_2)
				.lastName(LAST_NAME_2)
				.active(customer.isActive())
				.version(customer.getVersion())
				.build();
		
		// act
		CustomerResponse response = customerService.update(request);
		
		// assert
		Assertions.assertEquals(FIRST_NAME_2, response.getFirstName());
		Assertions.assertEquals(LAST_NAME_2, response.getLastName());
	}
	
	@Test
	@Transactional
	void updateCustomer_GivenInValidRequest_ThrowsOptimisticLockException() {
		
		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		var request = UpdateCustomerRequest.builder()
				.customerId(customer.getCustomerId())
				.firstName(FIRST_NAME_2)
				.lastName(LAST_NAME_2)
				.active(customer.isActive())
				.version(customer.getVersion())
				.build();
		
		customer.setActive(false);
		customerRepository.saveAndFlush(customer);
		
		// act and assert
		Assertions.assertThrows(OptimisticLockException.class, () -> customerService.update(request));
	}
	
	@Test
	@Transactional
	void updateCustomer_GivenInValidRequest_ThrowsUniqueConstraintException() {
		
		// arrange
		customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_2)
				.lastName(LAST_NAME_2)
				.build());
		
		var request = UpdateCustomerRequest.builder()
				.customerId(customer.getCustomerId())
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.active(customer.isActive())
				.version(customer.getVersion())
				.build();
		
		// act and assert
		Assertions.assertThrows(UniqueConstraintException.class, () -> customerService.update(request));
	}
}
