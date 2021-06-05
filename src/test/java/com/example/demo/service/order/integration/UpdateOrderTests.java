package com.example.demo.service.order.integration;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Order;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.exception.OptimisticLockException;
import com.example.demo.exception.UniqueConstraintException;

class UpdateOrderTests extends Setup {

	@Test
	@Transactional
	void updateOrder_GivenValidRequest_ReturnsOrderResponse() {
		
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
		
		// act
		OrderResponse response = orderService.update(request);
		
		// assert
		Assertions.assertEquals(ORDER_NUMBER_2, response.getOrderNumber());
		Assertions.assertEquals(OrderType.STORE, response.getType());
	}
	
	@Test
	@Transactional
	void updateOrder_GivenInValidRequest_ThrowsOptimisticLockException() {
		
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
				.version(order.getVersion() + 1)
				.build();
		
		// act and assert
		Assertions.assertThrows(OptimisticLockException.class, () -> orderService.update(request));
	}
	
	@Test
	@Transactional
	void updateOrder_GivenInValidRequest_ThrowsUniqueConstraintException() {
		
		// arrange
		orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.WEB)
				.build());
		
		var order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(ORDER_NUMBER_2)
				.type(OrderType.WEB)
				.build());
		
		var request = UpdateOrderRequest.builder()
				.orderId(order.getOrderId())
				.orderNumber(ORDER_NUMBER_1)
				.type(OrderType.STORE)
				.active(order.isActive())
				.version(order.getVersion())
				.build();
		
		// act and assert
		Assertions.assertThrows(UniqueConstraintException.class, () -> orderService.update(request));
	}
}
