package com.example.demo.controller.customer.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.model.Customer;
import com.example.demo.dto.UpdateCustomerRequest;
import com.example.demo.util.JsonUtil;

class UpdateCustomerTests extends Setup {

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void updateCustomer_GivenValidRole_ReturnsCustomerResponse() throws Exception {

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
		
		// act and assert
		mockMvc.perform(
				put("/Customer")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(request.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(request.getLastName())));
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void updateCustomer_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		var request = UpdateCustomerRequest.builder()
				.customerId(UUID.randomUUID())
				.firstName(NAME_1)
				.lastName(NAME_1)
				.active(false)
				.version(0)
				.build();
		
		//act and assert
		mockMvc.perform(
				put("/Customer")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
