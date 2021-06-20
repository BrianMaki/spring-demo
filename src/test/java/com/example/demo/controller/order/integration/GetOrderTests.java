package com.example.demo.controller.order.integration;

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

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Order;

class GetOrderTests extends Setup {

	@Test
	@Transactional
	void getOrder_ReturnsCustomerResponseList() throws Exception {

		// arrange
		orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_2)
				.type(OrderType.STORE)
				.build());

		// act and assert
		mockMvc.perform(
				get("/Order"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void getOrderByOrderId_GivenValidRole_ReturnsOrderResponse() throws Exception {

		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.STORE)
				.build());

		// act and assert
		mockMvc.perform(
				get("/Order/{orderId}", order.getOrderId().toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.orderId", is(order.getOrderId().toString())))
				.andExpect(jsonPath("$.orderNumber", is(order.getOrderNumber())))
				.andExpect(jsonPath("$.type", is(order.getType().toString())))
				.andExpect(jsonPath("$.active", is(true)));
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "invalid-spring-demo-api-client")
	void getOrderByOrderId_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.STORE)
				.build());

		// act and assert
		mockMvc.perform(
				get("/Order/{orderId}", order.getOrderId().toString()))
				.andExpect(status().isInternalServerError());
	}
}
