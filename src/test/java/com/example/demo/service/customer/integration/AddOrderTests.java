package com.example.demo.service.customer.integration;

import javax.transaction.Transactional;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Customer;
import com.example.demo.domain.model.Order;
import com.example.demo.dto.CreateCustomerOrderRequest;
import com.example.demo.dto.CustomerOrderResponse;

class AddOrderTests extends Setup {

	@Test
	@Transactional
	void addOrder_GivenOrderDoesNotExist_CreatesNewOrderForCustomer() {
		
		// arrange
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName("First Name")
				.lastName("Last Name")
				.build());
		
		var request = CreateCustomerOrderRequest.builder()
				.customerId(customer.getCustomerId())
				.orderNumber(RandomString.make())
				.type(OrderType.WEB)
				.build();
		
		// act
		CustomerOrderResponse response = customerService.addOrder(request);
		
		// assert
		Assertions.assertNotNull(response.getCustomerOrderId());
		Assertions.assertNotNull(response.getCustomerResponse());
		Assertions.assertNotNull(response.getCustomerResponse().getCustomerId());
		Assertions.assertNotNull(response.getOrderResponse());
		Assertions.assertNotNull(response.getOrderResponse().getOrderId());
		Assertions.assertEquals(request.getType(), response.getOrderResponse().getType());
	}
	
	@Test
	@Transactional
	void addOrder_GivenOrderExists_AssignsExistingOrderForCustomer() {
		
		// arrange
		String orderNumber = RandomString.make();
		
		var customer = customerRepository.saveAndFlush(Customer.builder()
				.firstName("First Name")
				.lastName("Last Name")
				.build());
		
		var request = CreateCustomerOrderRequest.builder()
				.customerId(customer.getCustomerId())
				.orderNumber(orderNumber)
				.type(OrderType.STORE)
				.build();
		
		Order order = orderRepository.saveAndFlush(Order.builder()
				.orderNumber(orderNumber)
				.type(OrderType.WEB)
				.build());
		
		// act
		CustomerOrderResponse response = customerService.addOrder(request);
		
		// assert
		Assertions.assertNotNull(response.getCustomerOrderId());
		Assertions.assertNotNull(response.getCustomerResponse());
		Assertions.assertNotNull(response.getCustomerResponse().getCustomerId());
		Assertions.assertNotNull(response.getOrderResponse());
		Assertions.assertEquals(order.getOrderId(), response.getOrderResponse().getOrderId());
		Assertions.assertEquals(request.getType(), response.getOrderResponse().getType());
	}
}
