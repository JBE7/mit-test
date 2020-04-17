package com.mitrol.example.ticketAdmin.exception;

public class NoResultException extends Exception {
	private static final long serialVersionUID = 5795692980404685983L;

	public NoResultException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoResultException(String message) {
		super(message);
	}

	public NoResultException(Throwable cause) {
		super("No result found!", cause);
	}

}
