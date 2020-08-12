package com.covidservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.covidservice.exceptionHandling.InvalidDateException;
import com.covidservice.exceptionHandling.TravelHistoryException;

@RestControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler{
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(TravelHistoryException.class)
	public ResponseEntity<String> handleTravelHistoryException(TravelHistoryException ex){
		LOG.error("Travel History Exception {}",ex.getMessage());
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR );
	}
	
	@ExceptionHandler(InvalidDateException.class)
	public ResponseEntity<String> handleInvalidDateException(InvalidDateException ex){
		LOG.error("Invalid date Exception {}",ex.getMessage());
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST );
	}
}
