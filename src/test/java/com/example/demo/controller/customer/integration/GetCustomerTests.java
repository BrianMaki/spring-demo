package com.example.demo.controller.customer.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.model.Customer;

class GetCustomerTests extends Setup {

	@Test
	@Transactional
	void getCustomer_ReturnsCustomerResponseList() throws Exception {

		// arrange
		customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_2)
				.lastName(LAST_NAME_2)
				.build());

		// act and assert
		mockMvc.perform(
				get("/Customer"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void getCustomerByCustomerId_GivenValidRole_ReturnsCustomerResponse() throws Exception {

		// arrange
		Customer customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());

		// act and assert
		mockMvc.perform(
				get("/Customer/{customerId}", customer.getCustomerId().toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.customerId", is(customer.getCustomerId().toString())))
				.andExpect(jsonPath("$.firstName", is(customer.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(customer.getLastName())))
				.andExpect(jsonPath("$.active", is(true)));
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "invalid-spring-demo-api-client")
	void getCustomerByCustomerId_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		Customer customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());

		// act and assert
		mockMvc.perform(
				get("/Customer/{customerId}", customer.getCustomerId().toString()))
				.andExpect(status().isInternalServerError());
	}
}
