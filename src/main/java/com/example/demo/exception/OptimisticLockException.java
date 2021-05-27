package com.example.demo.exception;

public class OptimisticLockException extends RuntimeException {

	private static final long serialVersionUID = 5963976555756247840L;

	public OptimisticLockException(String message) {
		super(message);
	}
}
