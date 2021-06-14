package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Order;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.OptimisticLockException;
import com.example.demo.exception.UniqueConstraintException;
import com.example.demo.projection.OrderView;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.ExceptionUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class OrderService {
	
	private final ModelMapper modelMapper;
	private final OrderRepository orderRepository;
	
	public OrderResponse create(CreateOrderRequest request) {
		
		log.info("Creating Order for: {}", request.toString());
		
		try {
			return modelMapper.map(orderRepository.saveAndFlush(modelMapper.map(request, Order.class)), OrderResponse.class);
			
		} catch (Exception e) {
			if (ExceptionUtils.isCause(DataIntegrityViolationException.class, e)) {
				throw new UniqueConstraintException("Order Number must be unique: " + request.getOrderNumber());
			}
			throw e;
		}
	}
	
	public OrderResponse update(UpdateOrderRequest request) {
		
		var order = orderRepository.findById(request.getOrderId())
				.orElseThrow(() -> new EntityNotFoundException("Unable to update Order because could not find Order with order id: " + request.getOrderId()));
		
		if (order.getVersion() != request.getVersion()) {
			throw new OptimisticLockException("Unable to update Order because record has changed for Order with order id: " + request.getOrderId());
		}
		
		modelMapper.map(request, order);
		
		try {
			return modelMapper.map(orderRepository.saveAndFlush(order), OrderResponse.class);
			
		} catch (Exception e) {
			if (ExceptionUtils.isCause(DataIntegrityViolationException.class, e)) {
				throw new UniqueConstraintException("Order Number must be unique: " + request.getOrderNumber());
			} else if (ExceptionUtils.isCause(OptimisticLockingFailureException.class, e)) {
				throw new OptimisticLockException("Unable to update Order because record has changed for Order with order id:" + request.getOrderId());
			}
			throw e;
		}
	}
	
	public OrderView get(UUID orderId) {
		
		OrderView view = orderRepository.findByOrderId(orderId, OrderView.class);
		
		if (view == null) {
			throw new EntityNotFoundException("Could not find Order with order id: " + orderId);
		}
		return view;
	}
	
	public List<OrderResponse> get() {
		
		List<OrderResponse>responses = new ArrayList<>();
		List<Order> orders = orderRepository.findAll();
		orders.forEach(order -> responses.add(modelMapper.map(order, OrderResponse.class)));
		return responses;
	}
	
	public void delete(UUID orderId) {
		orderRepository.deleteById(orderId);
	}
}
