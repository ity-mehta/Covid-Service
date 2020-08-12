package com.covidservice.exceptionHandling;

public class InvalidDateException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public InvalidDateException(String msg) {
		super(msg);
	}


}
