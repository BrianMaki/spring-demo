package com.example.demo.controller.customer.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.model.Customer;

class DeleteCustomerTests extends Setup {

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void deleteCustomer_GivenValidRole_ReturnsNoContent() throws Exception {

		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		// act and assert
		mockMvc.perform(
				delete("/Customer")
				.param("customerId", customer.getCustomerId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void deleteCustomer_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		//act and assert
		mockMvc.perform(
				delete("/Customer")
				.param("customerId", customer.getCustomerId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
