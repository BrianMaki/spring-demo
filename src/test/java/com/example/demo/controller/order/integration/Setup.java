package com.example.demo.controller.order.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("unit-test")
class Setup {
	
	protected static final String ORDER_NUMBER_1 = RandomString.make();
	protected static final String ORDER_NUMBER_2 = RandomString.make();

	@Autowired
	protected OrderRepository orderRepository;
	
	@Autowired
	protected OrderService orderService;
	
	@Autowired
	protected WebApplicationContext context;
	
    protected MockMvc mockMvc;
    
    @BeforeEach
    void setUp() throws Exception {
    	
    	mockMvc = MockMvcBuilders.webAppContextSetup(context)
    			.alwaysDo(print())
    		    //.apply(springSecurity())
    			.build(); 
        
    }
}
