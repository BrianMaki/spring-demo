package com.example.demo.projection;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;



public interface CustomerView {
	UUID getCustomerId();
	String getFirstName();
	String getLastName();
	
	@Value("#{target.firstName + ' ' + target.lastName}")
	String getName();
	
	boolean isActive();
	long getVersion();
}
