package com.example.demo.projection;

import java.util.UUID;

import com.example.demo.domain.enums.OrderType;

public interface OrderView {
	
	UUID getOrderId();
	String getOrderNumber();
	OrderType getType();
	boolean isActive();
	long getVersion();
}
