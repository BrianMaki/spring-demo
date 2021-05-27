package com.example.demo.projection;

import java.util.UUID;

public interface OrderView {
	
	UUID getOrderId();
	String getOrderNumber();
	boolean isActive();
	long getVersion();
}
