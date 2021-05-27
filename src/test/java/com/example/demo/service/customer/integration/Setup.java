package com.example.demo.service.customer.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.SpringDemoApplication;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CustomerService;

@SpringBootTest(classes = SpringDemoApplication.class)
@TestPropertySource(locations = "classpath:application-unittest.properties")
class Setup {

	@Autowired
	protected CustomerRepository customerRepository;
	
	@Autowired
	protected OrderRepository orderRepository;
	
	@Autowired
	protected CustomerService customerService;
}
