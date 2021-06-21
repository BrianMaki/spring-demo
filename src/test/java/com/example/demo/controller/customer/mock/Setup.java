package com.example.demo.controller.customer.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.controller.CustomerController;
import com.example.demo.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class Setup {

	protected static final String FIRST_NAME_1 = "First Name 1";
	protected static final String LAST_NAME_1 = "Last Name 1";
	protected static final String NAME_1 = FIRST_NAME_1 + " " + LAST_NAME_1;

	protected static final String FIRST_NAME_2 = "First Name 2";
	protected static final String LAST_NAME_2 = "Last Name 2";

	@Mock
	protected CustomerService customerService;

	protected MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new CustomerController(customerService))
				.build();
	}
}
