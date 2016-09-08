package com.exceptions;

public class WeatherApiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WeatherApiException(){
		
	}
	public WeatherApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
