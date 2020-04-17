package com.mitrol.example.ticketAdmin.exception;

public class InvalidException extends Exception {
	private static final long serialVersionUID = 1543000892677919436L;

	public InvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidException(String message) {
		super(message);
	}

	public InvalidException(Throwable cause) {
		super("Validation error!", cause);
	}

}
