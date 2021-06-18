package com.example.demo.service.customer.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.SpringDemoApplication;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CustomerService;

@ActiveProfiles("unit-test")
@SpringBootTest(classes = SpringDemoApplication.class)
class Setup {
	
	protected static final String FIRST_NAME_1 = "First Name 1";
	protected static final String LAST_NAME_1 = "Last Name 1"; 
	protected static final String NAME_1 = FIRST_NAME_1 + " " + LAST_NAME_1;
	
	protected static final String FIRST_NAME_2 = "First Name 2";
	protected static final String LAST_NAME_2 = "Last Name 2";

	@Autowired
	protected CustomerRepository customerRepository;
	
	@Autowired
	protected OrderRepository orderRepository;
	
	@Autowired
	protected CustomerService customerService;
}
