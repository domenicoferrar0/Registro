package com.ferraro.RegistroScolastico.exceptions;

public class ConfirmationTokenNotFoundException extends RuntimeException{
	
	public ConfirmationTokenNotFoundException(String token) {
		super("Impossibile trovare questo token di conferma: \n".concat(token));
	}
}
