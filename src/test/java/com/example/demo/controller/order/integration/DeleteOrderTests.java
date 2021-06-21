package com.example.demo.controller.order.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Order;

class DeleteOrderTests extends Setup {

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void deleteOrder_GivenValidRole_ReturnsOrderResponse() throws Exception {

		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		// act and assert
		mockMvc.perform(
				delete("/Order")
				.param("orderId", order.getOrderId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void deleteOrder_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		//act and assert
		mockMvc.perform(
				delete("/Order")
				.param("orderId", order.getOrderId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
