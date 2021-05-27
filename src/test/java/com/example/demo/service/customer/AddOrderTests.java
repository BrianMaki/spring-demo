package com.example.demo.service.customer;

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
		
		Customer customer = Customer.builder()
				.customerId(customerId)
				.firstName("First Name")
				.lastName("Last Name")
				.build();
		
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		
		Order existingOrder = Order.builder()
				.orderId(UUID.randomUUID())
				.orderNumber(orderNumber)
				.build();
		
		when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(existingOrder));
		
		CustomerOrder customerOrder = CustomerOrder.builder()
				.customer(customer)
				.order(existingOrder)
				.build();

		CreateCustomerOrderRequest request = CreateCustomerOrderRequest.builder()
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
		
		Customer customer = Customer.builder()
				.customerId(customerId)
				.firstName("First Name")
				.lastName("Last Name")
				.build();
		
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		
		when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());
		
		Order order = Order.builder()
				.orderId(UUID.randomUUID())
				.orderNumber(orderNumber)
				.build();
		
		CustomerOrder customerOrder = CustomerOrder.builder()
				.customer(customer)
				.order(order)
				.build();

		order.getCustomerOrders().add(customerOrder);
		
		CreateCustomerOrderRequest request = CreateCustomerOrderRequest.builder()
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
