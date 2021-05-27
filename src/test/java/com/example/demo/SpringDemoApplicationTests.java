package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = SpringDemoApplication.class)
@TestPropertySource(locations = "classpath:application-unittest.properties")
class SpringDemoApplicationTests {

	@Test
	void contextLoads() {
		
		String expected = "1";
		String actual = "1";
		Assertions.assertEquals(expected, actual);
	}

}
