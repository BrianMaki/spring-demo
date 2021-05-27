package com.example.demo.exception;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7175454409445086375L;

	public EntityNotFoundException(String message) {
		super(message);
	}
}
