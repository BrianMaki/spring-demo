package com.example.demo.service.order.integration;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Order;
import com.example.demo.dto.OrderResponse;
import com.example.demo.exception.EntityNotFoundException;

class GetOrderTests extends Setup {

	@Test
	@Transactional
	void getOrder_ReturnsOrderResponseList() {
		
		int expectedSize = 2;
		
		// arrange
		orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_2)
				.type(OrderType.STORE)
				.build());
		
		// act
		List<OrderResponse> list = orderService.get();
		
		// assert
		Assertions.assertEquals(expectedSize, list.size());
	}
	
	@Test
	@Transactional
	void getOrder_ReturnsEmptyOrderResponseList() {
		
		int expectedSize = 0;
		
		// arrange
		
		// act
		List<OrderResponse> list = orderService.get();
		
		// assert
		Assertions.assertEquals(expectedSize, list.size());
	}
	
	@Test
	@Transactional
	void getOrderById_GivenValidId_ReturnsOrderView() {
		
		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.STORE)
				.build());
		
		// act
		var view = orderService.get(order.getOrderId());
		
		// assert
		Assertions.assertNotNull(view);
		Assertions.assertEquals(ORDER_NUMBER_1, view.getOrderNumber());
	}
	
	@Test
	@Transactional
	void getOrderById_GivenInvalidId_ThrowsEntityNotFoundException() {
		
		// arrange
		UUID orderId = UUID.randomUUID();
		
		
		// act and assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> orderService.get(orderId));
	}

}
