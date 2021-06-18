package com.example.demo.controller.customer.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("unit-test")
class Setup {
	
	protected static final String FIRST_NAME_1 = "First Name 1";
	protected static final String LAST_NAME_1 = "Last Name 1"; 
	protected static final String NAME_1 = FIRST_NAME_1 + " " + LAST_NAME_1;
	
	protected static final String FIRST_NAME_2 = "First Name 2";
	protected static final String LAST_NAME_2 = "Last Name 2";
	
	@Autowired
	protected CustomerService customerService;
	
	@Autowired
	protected CustomerRepository customerRepository;
	
	@Autowired
	protected WebApplicationContext context;
	
    protected MockMvc mockMvc;
    
    @BeforeEach
    void setUp() throws Exception {
    	
    	mockMvc = MockMvcBuilders.webAppContextSetup(context)
    			//.alwaysDo(print())
    		    //.apply(springSecurity())
    			.build(); 
        
    }
}
