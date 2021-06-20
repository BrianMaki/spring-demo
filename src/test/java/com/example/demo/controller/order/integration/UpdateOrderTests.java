package com.example.demo.controller.order.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Order;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.util.JsonUtil;

class UpdateOrderTests extends Setup {

	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-admin")
	void updateOrder_GivenValidRole_ReturnsOrderResponse() throws Exception {

		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		var request = UpdateOrderRequest.builder()
				.orderId(order.getOrderId())
				.orderNumber(ORDER_NUMBER_2)
				.type(OrderType.STORE)
				.active(order.isActive())
				.version(order.getVersion())
				.build();
		
		// act and assert
		mockMvc.perform(
				put("/Order")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderNumber", is(request.getOrderNumber())))
				.andExpect(jsonPath("$.type", is(request.getType().toString())));
	}
	
	@Test
	@Transactional
	@WithMockUser(roles = "spring-demo-api-client")
	void updateOrder_GivenInvalidRole_ReturnsInternalServerError() throws Exception {

		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		var request = UpdateOrderRequest.builder()
				.orderId(order.getOrderId())
				.orderNumber(ORDER_NUMBER_2)
				.type(OrderType.STORE)
				.active(order.isActive())
				.version(order.getVersion())
				.build();
		
		//act and assert
		mockMvc.perform(
				put("/Order")
				.content(JsonUtil.asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
