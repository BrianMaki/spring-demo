package com.example.demo.controller.order.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.util.JsonUtil;

class CreateOrderTests extends Setup {

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void createCustomer_GivenValidRole_ReturnsCustomerResponse() throws Exception {
		
		// arrange
		var request = CreateOrderRequest.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build();

		// act and assert
		mockMvc.perform(
				post("/Order")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.orderId").exists());
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void createCustomer_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		var request = CreateOrderRequest.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build();
		
		// act and assert
		mockMvc.perform(
				post("/Order")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
