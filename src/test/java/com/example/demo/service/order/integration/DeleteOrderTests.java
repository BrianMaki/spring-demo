package com.example.demo.service.order.integration;

import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Order;
import com.example.demo.exception.EntityNotFoundException;

class DeleteOrderTests extends Setup {

	@Test
	@Transactional
	void deleteOrder_GivenValidOrderId() {
		
		// arrange
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		// act
		orderService.delete(order.getOrderId());
		
		// assert
		Assertions.assertTrue(orderRepository.findById(order.getOrderId()).isEmpty());
	}
	
	@Test
	@Transactional
	void deleteOrder_GivenInvalidOrderId_ThrowsEntityNotFoundException() {
		
		// arrange
		orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		UUID orderId = UUID.randomUUID();
		
		// act and assert
		Assertions.assertThrows(EntityNotFoundException.class, () -> orderService.delete(orderId));
	}
}
