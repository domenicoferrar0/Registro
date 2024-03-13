package com.ferraro.RegistroScolastico.exceptions;

public class CustomException extends RuntimeException {

	private Object object;
		

	public Object getObject() {
		return this.object;
	}

	public CustomException(String message, Object object) {
		super(message);
		this.object = object;
		
	}
}
