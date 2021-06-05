package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Customer;
import com.example.demo.domain.model.CustomerOrder;
import com.example.demo.domain.model.Order;
import com.example.demo.dto.CreateCustomerOrderRequest;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.dto.CustomerOrderResponse;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.UpdateCustomerRequest;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.OptimisticLockException;
import com.example.demo.exception.UniqueConstraintException;
import com.example.demo.projection.CustomerView;
import com.example.demo.repository.CustomerOrderRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.ExceptionUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class CustomerService {
	
	private final ModelMapper modelMapper;
	private final CustomerRepository customerRepository;
	private final CustomerOrderRepository customerOrderRepository;
	private final OrderRepository orderRepository;
	
	public CustomerResponse create(CreateCustomerRequest request) {
		
		log.info("Creating Customer for: {}", request.toString());
		
		try {
			return modelMapper.map(customerRepository.saveAndFlush(modelMapper.map(request, Customer.class)), CustomerResponse.class);
			
		} catch (Exception e) {
			if (ExceptionUtils.isCause(DataIntegrityViolationException.class, e)) {
				throw new UniqueConstraintException("First Name and Last Name must be unique: " + request);
			}
			throw e;
		}
	}
	
	public CustomerResponse update(UpdateCustomerRequest request) {
		
		Customer customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new EntityNotFoundException("Unable to update Customer because record could not find Customer with customer id: " + request.getCustomerId()));
		
		if (customer.getVersion() != request.getVersion()) {
			throw new OptimisticLockException("Unable to update Customer because record has changed for Customer with customer id: " + request.getCustomerId());
		}
		
		modelMapper.map(request, customer);
		
		try {
			return modelMapper.map(customerRepository.saveAndFlush(customer), CustomerResponse.class);
			
		} catch (Exception e) {
			if (ExceptionUtils.isCause(DataIntegrityViolationException.class, e)) {
				throw new UniqueConstraintException("First Name and Last Name must be unique: " + request);
			} else if (ExceptionUtils.isCause(OptimisticLockingFailureException.class, e)) {
				throw new OptimisticLockException("Unable to update Customer because record has changed for Customer with customer id:" + request.getCustomerId());
			}
			throw e;
		}
	}
	
	public CustomerView get(UUID customerId) {
		
		CustomerView view = customerRepository.findByCustomerId(customerId, CustomerView.class);
		
		if (view == null) {
			throw new EntityNotFoundException("Could not find Customer with customer id: " + customerId);
		}
		return view;
	}
	
	public List<CustomerResponse> get() {
		
		List<CustomerResponse> responses = new ArrayList<>();
		List<Customer> customers = customerRepository.findAll();
		customers.forEach(customer -> responses.add(modelMapper.map(customer, CustomerResponse.class)));
		return responses;
	}
	
	public void delete(UUID customerId) {
		customerRepository.deleteById(customerId);
	}
	
	public CustomerOrderResponse addOrder(CreateCustomerOrderRequest request) {
		
		Customer customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new EntityNotFoundException("Unable to find Customer to add Order for customer id: " + request.getCustomerId()));
		
		Optional<Order> existingOrder = orderRepository.findByOrderNumber(request.getOrderNumber());
		
		if (existingOrder.isPresent()) {
			
			modelMapper.map(request, existingOrder.get());
			CustomerOrder customerOrder = CustomerOrder.builder()
					.customer(customer)
					.order(existingOrder.get())
					.build();
			customerOrderRepository.saveAndFlush(customerOrder);

			return CustomerOrderResponse.builder()
					.customerOrderId(customerOrder.getCustomerOrderId())
					.customerResponse(modelMapper.map(customer, CustomerResponse.class))
					.orderResponse(modelMapper.map(existingOrder.get(), OrderResponse.class))
					.build();
			
		} else {
			
			Order order = modelMapper.map(request, Order.class);
			order.getCustomerOrders().add(CustomerOrder.builder()
					.customer(customer)
					.order(order)
					.build());
			orderRepository.saveAndFlush(order);
			
			return CustomerOrderResponse.builder()
					.customerOrderId(order.getCustomerOrders().iterator().next().getCustomerOrderId())
					.customerResponse(modelMapper.map(customer, CustomerResponse.class))
					.orderResponse(modelMapper.map(order, OrderResponse.class))
					.build();
		}
	}
}
