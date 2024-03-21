package com.ferraro.RegistroScolastico.exceptions;

public class ResourceNotFoundException extends RuntimeException{
	
	public ResourceNotFoundException(String risorsa) {
		super("Stai tentando di accedere ad una risorsa che non esiste ".concat(risorsa));
	}
}
