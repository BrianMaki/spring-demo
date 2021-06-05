package com.example.demo.service.customer.mock;

import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.Customer;
import com.example.demo.domain.model.CustomerOrder;
import com.example.demo.domain.model.Order;
import com.example.demo.dto.CreateCustomerOrderRequest;

class AddOrderTests extends Setup {

	@Test
	void addOrder_GivenOrderDoesNotExist_CreatesNewOrderForCustomer() {
		
		// arrange
		String orderNumber = RandomString.make();
		UUID customerId = UUID.randomUUID();
		
		var customer = Customer.builder()
				.customerId(customerId)
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.build();
		
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		
		var existingOrder = Order.builder()
				.orderId(UUID.randomUUID())
				.orderNumber(orderNumber)
				.build();
		
		when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(existingOrder));
		
		var customerOrder = CustomerOrder.builder()
				.customer(customer)
				.order(existingOrder)
				.build();

		var request = CreateCustomerOrderRequest.builder()
				.customerId(customer.getCustomerId())
				.orderNumber(orderNumber)
				.type(OrderType.WEB)
				.build();
		
		// act
		customerService.addOrder(request);
		
		// assert
		Mockito.verify(customerOrderRepository, Mockito.times(1)).saveAndFlush(customerOrder);
	}
	
	@Test
	void addOrder_GivenOrderExists_AssignsExistingOrderForCustomer() {
		
		// arrange
		String orderNumber = RandomString.make();
		UUID customerId = UUID.randomUUID();
		
		var customer = Customer.builder()
				.customerId(customerId)
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.build();
		
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		
		when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());
		
		var order = Order.builder()
				.orderId(UUID.randomUUID())
				.orderNumber(orderNumber)
				.build();
		
		var customerOrder = CustomerOrder.builder()
				.customer(customer)
				.order(order)
				.build();

		order.getCustomerOrders().add(customerOrder);
		
		var request = CreateCustomerOrderRequest.builder()
				.customerId(customer.getCustomerId())
				.orderNumber(orderNumber)
				.type(OrderType.WEB)
				.build();
		
		when(modelMapper.map(request, Order.class)).thenReturn(order);
		
		// act
		customerService.addOrder(request);
		
		// assert
		Mockito.verify(orderRepository, Mockito.times(1)).saveAndFlush(order);
	}
}
