package com.example.demo.service.order.integration;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.SpringDemoApplication;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;

@ActiveProfiles("unit-test")
@SpringBootTest(classes = SpringDemoApplication.class)
class Setup {

	protected static final String ORDER_NUMBER_1 = RandomString.make();
	protected static final String ORDER_NUMBER_2 = RandomString.make();

	@Autowired
	protected OrderRepository orderRepository;

	@Autowired
	protected OrderService orderService;
}
