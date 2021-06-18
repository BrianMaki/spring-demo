package com.example.demo.controller.customer.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.model.Customer;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.util.JsonUtil;

class GetCustomerTests extends Setup {

	@Test
	@Transactional
	@WithMockUser
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
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void getCustomer_GivenValidRole_ReturnsCustomerResponse() throws Exception {

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
				.andExpect(jsonPath("$.customerId", notNullValue()))
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME_1)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME_1)))
				.andExpect(jsonPath("$.active", is(true)))
				.andDo(print());
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "invalid-spring-demo-api-client")
	void getCustomer_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		Customer customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());

		// act and assert
		mockMvc.perform(
				get("/Customer/{customerId}", customer.getCustomerId().toString()))
				.andExpect(status().isInternalServerError())
				.andDo(print());
	}

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void createCustomer_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange, act and assert
		mockMvc.perform(
				post("/Customer")
				.content(JsonUtil.asJsonString( new CreateCustomerRequest(FIRST_NAME_1, LAST_NAME_1)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andDo(print());
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void createCustomer_GivenInvalidRole_ReturnsCustomerResponse() throws Exception {

		// arrange, act and assert
		mockMvc.perform(
				post("/Customer")
				.content(JsonUtil.asJsonString( new CreateCustomerRequest(FIRST_NAME_1, LAST_NAME_1)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerId").exists())
				.andDo(print());
	}
}
