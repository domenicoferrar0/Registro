package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.enums.Resource;

public class ResourceNotFoundException extends RuntimeException{
	
	public ResourceNotFoundException(Resource risorsa, Long id) {
		super(String.format("Errore, risorsa non trovata %s con Id: %d", risorsa.getNome(), id));
	}
}
