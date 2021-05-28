package com.example.demo.service.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.demo.repository.CustomerOrderRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class Setup {
	
	@Mock
	protected ModelMapper modelMapper;
	
	@Mock
	protected CustomerRepository customerRepository;
	
	@Mock
	protected CustomerOrderRepository customerOrderRepository;
	
	@Mock
	protected OrderRepository orderRepository;
	
	protected CustomerService customerService;
	
    @BeforeEach
    protected void init() {
    	
    	this.customerService = 
    			new CustomerService(this.modelMapper, this.customerRepository, this.customerOrderRepository, this.orderRepository);
    }
}
