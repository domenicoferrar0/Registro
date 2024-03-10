package com.ferraro.RegistroScolastico.exceptions;

public class ResourceNotFoundException extends RuntimeException{
	
	public ResourceNotFoundException(Long id) {
		super("Stai tentando di accedere ad una risorsa che non esiste id: ".concat(id.toString()));
	}
}
