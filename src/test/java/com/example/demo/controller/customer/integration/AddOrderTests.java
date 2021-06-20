package com.example.demo.controller.customer.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Customer;
import com.example.demo.dto.CreateCustomerOrderRequest;
import com.example.demo.util.JsonUtil;

class AddOrderTests extends Setup {

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void addOrder_GivenValidRole_ReturnsCustomerOrderResponse() throws Exception {
		
		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		var request = CreateCustomerOrderRequest.builder()
				.customerId(customer.getCustomerId())
				.orderNumber(RandomString.make())
				.type(OrderType.WEB)
				.build();

		// act and assert
		mockMvc.perform(
				post("/Customer/Order")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerOrderId").exists())
				.andExpect(jsonPath("$.orderResponse.orderId", notNullValue()))
				.andExpect(jsonPath("$.customerResponse.customerId", is(customer.getCustomerId().toString())));
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void addOrder_GivenInvalidRole_ReturnsInternalServerError() throws Exception {
		
		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.build());
		
		var request = CreateCustomerOrderRequest.builder()
				.customerId(customer.getCustomerId())
				.orderNumber(RandomString.make())
				.type(OrderType.WEB)
				.build();

		// act and assert
		mockMvc.perform(
				post("/Customer/Order")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
