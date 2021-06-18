package com.example.demo.controller.customer.mock;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.example.demo.dto.CustomerResponse;

class GetCustomerTests extends Setup {

	@Test
	void getCustomer_ReturnsCustomerResponseList() throws Exception {
		
		int expectedSize = 2;
		UUID customerId1 = UUID.randomUUID();
		UUID customerId2 = UUID.randomUUID();
		
		// arrange
		CustomerResponse response1 = CustomerResponse.builder()
				.customerId(customerId1)
				.firstName(FIRST_NAME_1)
				.lastName(LAST_NAME_1)
				.active(true)
				.build();
		
		CustomerResponse response2 = CustomerResponse.builder()
				.customerId(customerId2)
				.firstName(FIRST_NAME_2)
				.lastName(LAST_NAME_2)
				.active(true)
				.build();

		List<CustomerResponse> responses = new ArrayList<>();
		responses.add(response1);
		responses.add(response2);
		
		when(customerService.get()).thenReturn(responses);

		// act and assert
		mockMvc.perform(get("/Customer"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andExpect(jsonPath("$", hasSize(expectedSize)))
        	.andExpect(jsonPath("$[0].customerId", is(customerId1.toString())))
            .andExpect(jsonPath("$[0].firstName", is(response1.getFirstName())))
            .andExpect(jsonPath("$[0].lastName", is(response1.getLastName())))
            .andExpect(jsonPath("$[0].active", is(response1.isActive())))
            .andExpect(jsonPath("$[1].customerId", is(customerId2.toString())))
            .andExpect(jsonPath("$[1].firstName", is(response2.getFirstName())))
            .andExpect(jsonPath("$[1].lastName", is(response2.getLastName())))
            .andExpect(jsonPath("$[1].active", is(response2.isActive())))
        	.andDo(print());
	}
}
