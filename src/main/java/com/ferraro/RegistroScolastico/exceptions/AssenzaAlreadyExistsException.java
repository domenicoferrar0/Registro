package com.ferraro.RegistroScolastico.exceptions;

import java.time.LocalDate;

public class AssenzaAlreadyExistsException extends RuntimeException{
	
	public AssenzaAlreadyExistsException(String cf, LocalDate data) {
		super(String.format("Spiacente, %s lo studente risulta già assente in questa data %s", cf, data.toString()));
	}
	
}
