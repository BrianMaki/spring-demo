package com.example.demo.exception;

public class UniqueConstraintException extends RuntimeException {
    
	private static final long serialVersionUID = -8506106501081111086L;

	public UniqueConstraintException(String message) {
        super(message);
    }
}
