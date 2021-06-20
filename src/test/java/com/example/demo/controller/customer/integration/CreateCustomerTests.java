package com.example.demo.controller.customer.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.util.JsonUtil;

class CreateCustomerTests extends Setup {

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void createCustomer_GivenValidRole_ReturnsCustomerResponse() throws Exception {
		
		// arrange
		var request = CreateCustomerRequest.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build();

		// act and assert
		mockMvc.perform(
				post("/Customer")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerId").exists());
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void createCustomer_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		var request = CreateCustomerRequest.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build();
		
		// act and assert
		mockMvc.perform(
				post("/Customer")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
