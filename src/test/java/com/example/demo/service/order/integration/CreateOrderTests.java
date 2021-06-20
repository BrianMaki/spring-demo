package com.example.demo.service.order.integration;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Order;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.exception.UniqueConstraintException;

class CreateOrderTests extends Setup {

	@Test
	@Transactional
	void createOrder_GivenValidRequest_ReturnsOrderResponse() {
		
		// arrange
		var request = CreateOrderRequest.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build();
		
		// act
		OrderResponse response = orderService.create(request);
		
		// assert
		Assertions.assertNotNull(response.getOrderId());
		Assertions.assertEquals(ORDER_NUMBER_1, response.getOrderNumber());
	}
	
	@Test
	@Transactional
	void createOrder_GivenInvalidRequest_ThrowUniqueConstraintException() {
		
		// arrange
		orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		var request = CreateOrderRequest.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build();
		
		// act and assert
		Assertions.assertThrows(UniqueConstraintException.class, () -> orderService.create(request));
	}
}
